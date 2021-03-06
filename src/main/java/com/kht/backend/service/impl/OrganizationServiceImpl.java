package com.kht.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.CustAcctDOMapper;
import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.model.OrganizationModel;
import com.kht.backend.service.model.UserFromOrg;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class OrganizationServiceImpl implements OrganizationService {
    private static final String orgKey = "OrganizationList";
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;
    @Autowired
    private RedisServiceImpl redisService;
    @Resource
    private ValueOperations<String, Object> valueOperations;
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;


    @Transactional
    @Override
    public Result increaseOrganization(OrganizationDO organizationDO) {
        if (organizationDO == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "机构信息错误");
        }
        int affectRow = organizationDOMapper.insertSelective(organizationDO);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "新增机构失败");
        }
        List organizationList = (List) valueOperations.get(orgKey);
        organizationList.add(organizationDO);
        valueOperations.set(orgKey, organizationList);
        return Result.OK("新增机构成功").build();
    }

    @Transactional
    @Override
    public Result decreaseOrganization(String organizationId) {
        if (organizationId == null || organizationId.equals("")) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "机构编号不存在");
        }
        OrganizationDO organizationDO = organizationDOMapper.selectByPrimaryKey(organizationId);
        if (organizationDO == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "机构不存在，不需要删除");
        }
        int affectRow = organizationDOMapper.deleteByPrimaryKey(organizationId);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "删除机构失败");
        }
        List orgList = (List) valueOperations.get(orgKey);
        for (int i = 0; i < orgList.size(); i++) {
            OrganizationDO remove = (OrganizationDO) orgList.get(i);
            if (remove.getOrgCode().equals(organizationId)) {
                orgList.remove(remove);
            }
        }
        valueOperations.set(orgKey, orgList);
        return Result.OK("删除机构成功").build();
    }

    @Override
    public Map<String, Object> getOrganizationList(int pageNum) {
        Page<Object> pages = PageHelper.startPage(pageNum, Integer.parseInt(redisService.getParaValue("pageSize")));
        List<OrganizationDO> organizationDOList = organizationDOMapper.selectAll();
        List<OrganizationModel> organizationModelList = organizationDOList.stream().map(organizationDO -> {
            OrganizationModel organizationModel = this.convertFromDO(organizationDO);
            return organizationModel;
        }).collect(Collectors.toList());
        if (organizationModelList == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "机构不存在，请等待机构添加");
        }
        System.out.println(organizationDOList.size());
        PageInfo<OrganizationModel> page = new PageInfo<>(organizationModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("pageSize", redisService.getParaValue("pageSize"));
        resultData.put("organization_num", pages.getTotal());
        resultData.put("organizations", page.getList());
        return resultData;
    }

    @Transactional
    @Override
    public Result modifyOrganizationInfo(OrganizationDO organizationDO) {
        OrganizationDO organizationDO1 = organizationDOMapper.selectByPrimaryKey(organizationDO.getOrgCode());
        if (organizationDO1 == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "机构不存在，不能修改信息");
        }
        int affectRow = organizationDOMapper.updateByPrimaryKeySelective(organizationDO);
        List orgList = (List) valueOperations.get(orgKey);
        for (int i = 0; i < orgList.size(); i++) {
            OrganizationDO organizationDO2 = (OrganizationDO) orgList.get(i);
            if (organizationDO2.getOrgCode().equals(organizationDO.getOrgCode())) {
                orgList.remove(i);
                orgList.add(organizationDO);
                break;
            }
        }
        valueOperations.set(orgKey, orgList);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "机构修改失败");
        }
        return Result.OK("修改机构信息成功").build();
    }

    @Override
    public Result getOrganizationUser(String orgCode, int pageNum,boolean isAudit) {
        Page<Object> pages = PageHelper.startPage(pageNum, Integer.parseInt(redisService.getParaValue("pageSize")));
        List<AcctOpenInfoDO> custAcctDOList;
        if(isAudit){
            custAcctDOList = acctOpenInfoDOMapper.listAllByOrg(orgCode);
        }
        else{
            custAcctDOList = acctOpenInfoDOMapper.listUnauditedUserByOrg(orgCode);
        }
        List<UserFromOrg> userFromOrgList = custAcctDOList.stream().map(acctOpenInfoDO -> {
            UserFromOrg userFromOrg = this.convertFromDataObject(acctOpenInfoDO);
            userFromOrg.setIdType(redisService.getDataDictionary("ID_TYPE", "cust_acct", acctOpenInfoDO.getIdType()));
            userFromOrg.setOrgName(redisService.getOrganizationName(acctOpenInfoDO.getOrgCode()));
            return userFromOrg;
        }).collect(Collectors.toList());
        if (userFromOrgList == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "该机构编号下的用户不存在");
        }
        PageInfo<UserFromOrg> page = new PageInfo<>(userFromOrgList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("pageSize", redisService.getParaValue("pageSize"));
        resultData.put("totalNum", pages.getTotal());
        resultData.put("userList", page.getList());
        return Result.OK(resultData).build();
    }

    @Override
    public Result getOrganizationById(String orgCode) {
        return null;
    }


    @Override
    public List getOrgByName(String orgName) {
        if (orgName == null || orgName.isEmpty()) {
            return null;
        }
        List<OrganizationDO> organizationDOList = organizationDOMapper.selectByName(orgName);
        if (organizationDOList == null || organizationDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "暂无此机构列表");
        }
        List<OrganizationModel> organizationModelList = organizationDOList.stream().map(organizationDO -> {
            OrganizationModel organizationModel = this.convertFromDO(organizationDO);
            return organizationModel;
        }).collect(Collectors.toList());
        return organizationModelList;
    }

    private UserFromOrg convertFromDataObject(AcctOpenInfoDO acctOpenInfoDO) {
        if (acctOpenInfoDO == null) {
            return null;
        }
        UserFromOrg userFromOrg = new UserFromOrg();
        BeanUtils.copyProperties(acctOpenInfoDO, userFromOrg);
        return userFromOrg;
    }

    private OrganizationModel convertFromDO(OrganizationDO organizationDO) {
        OrganizationModel organizationModel = new OrganizationModel();
        BeanUtils.copyProperties(organizationDO, organizationModel);
        int userNum;
        userNum = custAcctDOMapper.getUserCountByOrgCode(organizationDO.getOrgCode());
        int todayUserNum;
        int totalUserNum;
        long dateForToday = 0;
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
        String date = smf.format(new Date());
        String replace = date.replace("-", "");
        dateForToday = Long.parseLong(replace);
        totalUserNum = custAcctDOMapper.getAllCount(organizationDO.getOrgCode());
        todayUserNum = custAcctDOMapper.getTodayCount(dateForToday, organizationDO.getOrgCode());
        organizationModel.setUserNum(userNum);
        organizationModel.setOrgName(organizationModel.getOrgCode() + "-" + organizationModel.getOrgName());
        organizationModel.setTotalUserNum(totalUserNum);
        organizationModel.setTodayUserNum(todayUserNum);
        return organizationModel;
    }

}
