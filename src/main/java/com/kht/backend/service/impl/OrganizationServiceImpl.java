package com.kht.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
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
    private RedisTemplate redisTemplate;
    @Resource
    private ValueOperations<String,Object> valueOperations;
    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Transactional
    @Override
    public Result increaseOrganization(OrganizationDO organizationDO) {
        if(organizationDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构信息错误");
        }
        int affectRow = organizationDOMapper.insertSelective(organizationDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"新增机构失败");
        }
        List organizationList = (List)valueOperations.get(orgKey);
        organizationList.add(organizationDO);
        valueOperations.set(orgKey,organizationList);
        return Result.OK("新增机构成功").build();
    }

    @Transactional
    @Override
    public Result decreaseOrganization(String organizationId) {
        if (organizationId == null || organizationId.equals("")){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构编号不存在");
        }
        OrganizationDO organizationDO = organizationDOMapper.selectByPrimaryKey(organizationId);
        if(organizationDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构不存在，不需要删除");
        }
        int affectRow = organizationDOMapper.deleteByPrimaryKey(organizationId);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"删除机构失败");
        }
        List orgList = (List)valueOperations.get(orgKey);
        for(int i =0;i<orgList.size();i++){
            OrganizationDO remove = (OrganizationDO)orgList.get(i);
            if(remove.getOrgCode().equals(organizationId)){
                orgList.remove(remove);
            }
        }
        valueOperations.set(orgKey,orgList);
        return Result.OK("删除机构成功").build();
    }

    @Override
    public Map<String, Object> getOrganizationList(int pageNum) {
        PageHelper.startPage(pageNum,10);
        String key = "OrganizationList";
        List orgList = (List) valueOperations.get(key);
        if(orgList == null || orgList.isEmpty()){
            List<OrganizationDO> organizationDOList = organizationDOMapper.selectAll();
            List<OrganizationModel> organizationModelList = organizationDOList.stream().map(organizationDO -> {
                OrganizationModel organizationModel = new OrganizationModel();
                BeanUtils.copyProperties(organizationDO,organizationModel);
                int userNum;
                userNum = custAcctDOMapper.getUserCountByOrgCode(organizationDO.getOrgCode());
                organizationModel.setUserNum(userNum);
                return organizationModel;
            }).collect(Collectors.toList());
            valueOperations.set(key,organizationModelList);
            if(organizationModelList == null){
                throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构不存在，请等待机构添加");
            }
            PageInfo<OrganizationModel> page = new PageInfo<>(organizationModelList);
            Map<String,Object> resultData = new LinkedHashMap<>();
            resultData.put("organization_num",page.getTotal());
            resultData.put("organizations",page.getList());
            return resultData;
        }
        System.out.println("redis_有");
        PageInfo<OrganizationModel> page = new PageInfo<>(orgList);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("organization_num",page.getTotal());
        resultData.put("organizations",page.getList());
        return resultData;
    }

    @Transactional
    @Override
    public Result modifyOrganizationInfo(OrganizationDO organizationDO) {
        OrganizationDO organizationDO1 = organizationDOMapper.selectByPrimaryKey(organizationDO.getOrgCode());
        if(organizationDO1 == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构不存在，不能修改信息");
        }
        int affectRow = organizationDOMapper.updateByPrimaryKeySelective(organizationDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构修改失败");
        }
        return Result.OK("修改机构信息成功").build();
    }

    @Override
    public Result getOrganizationUser(String orgCode,int pageNum) {
        List<CustAcctDO> custAcctDOList = custAcctDOMapper.selectCustCodeByOrgCode(orgCode);
        List<UserFromOrg> userFromOrgList = custAcctDOList.stream().map(custAcctDO -> {
            UserFromOrg userFromOrg = this.convertFromDataObject(custAcctDO);
            return userFromOrg;
        }).collect(Collectors.toList());
        if(userFromOrgList == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"该机构编号下的用户不存在");
        }
        return Result.OK(userFromOrgList).build();

    }

    //TODO
    @Override
    public Result getOrganizationById(String orgCode) {
       /* @Override
   public Result getOrganizationById(String orgCode) {
        String orgKey = "organization";
        OrganizationDO organizationDO = redisTempleService.get(orgKey, OrganizationDO.class);
        //String json = jedisCluster.get(employeeKey);
        OrganizationDO organizationDO1 = new OrganizationDO();
        if( organizationDO == null){
            *//*if(json == null || "".equals(json) || "null".equalsIgnoreCase(json)){*//*
            organizationDO1 = organizationDOMapper.selectByPrimaryKey(orgCode);
            if(organizationDO1 == null){
                throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"获取员工信息失败");
            }
            redisTempleService.set(orgKey,organizationDO1);
            System.out.println(JSONObject.toJSONString(redisTempleService.get(orgKey,OrganizationDO.class)));
            return Result.OK(organizationDO1).build();
        }
        return  Result.OK(organizationDO).build();
    }*/
        return null;
    }

    @Override
    public List getOrgByName(String orgName) {
        if(orgName == null || orgName.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"输入关键字为空");
        }
        List<OrganizationDO> organizationDOList = organizationDOMapper.selectByName(orgName);
        if(organizationDOList == null || organizationDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"暂无此机构列表");
        }
        return organizationDOList;
    }
    private UserFromOrg convertFromDataObject(CustAcctDO custAcctDO){
        if(custAcctDO == null){
            return null;
        }
        UserFromOrg userFromOrg = new UserFromOrg();
        BeanUtils.copyProperties(custAcctDO,userFromOrg);
        return userFromOrg;
    }

}
