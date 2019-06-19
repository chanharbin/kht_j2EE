package com.kht.backend.service;

import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.model.OrganizationModel;

public interface OrganizationService {
    //增加机构
    public Result increaseOrganization(OrganizationDO organizationDO);
    //删除机构
    public Result decreaseOrganization(String organizationId);
    //获取所有机构列表
    public Result getOrganizationList(int pageNum);
    //修改机构信息
    public Result modifyOrganizationInfo(OrganizationDO organizationDO);
    //获取机构下所有的用户列表
    public Result getOrganizationUser(String orgCode,int pageNum);
}
