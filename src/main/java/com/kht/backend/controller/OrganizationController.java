package com.kht.backend.controller;

import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;
    /*
    * 获取机构列表
    * */
    @RequestMapping(value = "/organization",method = GET)
    public Result getOrganization(@RequestParam("page_num") int pageNum){
        Map orgResult = organizationService.getOrganizationList(pageNum);
        return Result.OK(orgResult).build();
    }

    //新增机构
    @RequestMapping(value = "/organization", method = POST)
    public Result increaseOrg(@RequestParam("ORG_NAME") String orgName,
                              @RequestParam("ORG_CODE") String orgCode,
                              @RequestParam("ORG_TEL") Long orgtel,
                              @RequestParam("ORG_ADDR") String orgAddr){
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrgTel(orgtel);
        organizationDO.setOrgAddr(orgAddr);
        organizationDO.setOrgName(orgName);
        organizationDO.setOrgCode(orgCode);
        Result result = organizationService.increaseOrganization(organizationDO);
        return result;
    }
    //修改机构
    @RequestMapping(value = "/organization",method = PUT)
    public Result modifyOrg(@RequestParam("ORG_NAME") String orgName,
                            @RequestParam("ORG_CODE") String orgCode,
                            @RequestParam("ORG_TEL") Long orgtel,
                            @RequestParam("ORG_ADDR") String orgAddr){
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrgTel(orgtel);
        organizationDO.setOrgAddr(orgAddr);
        organizationDO.setOrgName(orgName);
        organizationDO.setOrgCode(orgCode);
        Result result = organizationService.modifyOrganizationInfo(organizationDO);
        return result;
    }
    //删除机构
    @RequestMapping(value = "/organization",method = DELETE)
    public Result deleteOrg(@RequestParam("ORG_CODE")String orgCode){
        Result result = organizationService.decreaseOrganization(orgCode);
        return result;
    }






}
