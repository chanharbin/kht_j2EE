package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.OrganizationDOMapper;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;

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
        return Result.OK("删除机构成功").build();

    }

    @Override
    public Result getOrganizationList(int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<OrganizationDO> organizationDOList = organizationDOMapper.selectAll();
        List<OrganizationDO> organizationDOListFiltered = organizationDOList.stream().filter(organizationDO -> !organizationDO.getOrgCode().isEmpty()).collect(Collectors.toList());
        if(organizationDOListFiltered == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"机构不存在，请等待机构添加");
        }
        PageInfo<OrganizationDO> page = new PageInfo<>(organizationDOListFiltered);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();

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
        List<UserFromOrg> userFromOrgs = acctOpenInfoDOMapper.selectByOrgCode(orgCode);

        return null;

    }

}
