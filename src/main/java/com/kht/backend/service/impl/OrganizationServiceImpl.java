package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.model.OrganizationModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    private OrganizationDOMapper organizationDOMapper;

    @Override
    @Transactional
    public Result increaseOrganization(OrganizationModel organizationModel) {
        OrganizationDO organizationDO = new OrganizationDO();
        BeanUtils.copyProperties(organizationModel,organizationDO);
        int affectRow=organizationDOMapper.insertSelective(organizationDO);
        if(affectRow<=0)
            throw  new ServiceException(ErrorCode.SERVER_EXCEPTION,"添加机构失败");
        return Result.OK("添加机构成功").build();
    }

    @Override
    @Transactional
    public Result decreaseOrganization(String organizationId) {
        int affectRow=organizationDOMapper.deleteByPrimaryKey(organizationId);
        if(affectRow<=0)
            throw  new ServiceException(ErrorCode.SERVER_EXCEPTION,"删除机构失败");
        return Result.OK("删除机构成功").build();
    }

    @Override
    public Result getOrganization(int pageNum) {
        //待改
        PageHelper.startPage(pageNum,10);
        List<OrganizationDO> organizationDOList=organizationDOMapper.selectAll();
        List<OrganizationModel> organizationModelList=organizationDOList.stream().map(organizationDO -> {
            OrganizationModel organizationModel= new OrganizationModel();
            BeanUtils.copyProperties(organizationDO,organizationModel);
            return organizationModel;
        }).collect(Collectors.toList());
        PageInfo<OrganizationDO> page = new PageInfo<>(organizationDOList);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("organization_num",page.getTotal());
        resultData.put("organizations",organizationModelList);
        return Result.OK(resultData).build();
    }

    @Override
    public Result updateOrganization(OrganizationModel organizationModel) {
        OrganizationDO organizationDO = new OrganizationDO();
        BeanUtils.copyProperties(organizationModel,organizationDO);
        int affectRow=organizationDOMapper.updateByPrimaryKeySelective(organizationDO);
        if(affectRow<=0)
            throw  new ServiceException(ErrorCode.SERVER_EXCEPTION,"更新机构失败");
        return Result.OK("更新机构成功").build();
    }
}
