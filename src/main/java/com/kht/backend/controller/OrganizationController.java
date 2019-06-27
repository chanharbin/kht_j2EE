package com.kht.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.model.OrgListModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class OrganizationController {
    @Autowired
    private OrganizationService organizationService;

    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private ValueOperations<String,Object> valueOperations;
    /*
    * 获取机构列表
    * */
    @RequestMapping(value = "/organization",method = GET)
    public Result getOrganization(@RequestParam("page_num") int pageNum){
        Map orgResult = organizationService.getOrganizationList(pageNum);
        return Result.OK(orgResult).build();
    }
    @RequestMapping(value = "/organizationList",method = GET)
    public Result getOrganization(){
        List<OrganizationDO> organizationList = (List<OrganizationDO>)valueOperations.get("OrganizationList");
        List organizationListFilterd = organizationList.stream().map(organizationDO -> {
            OrgListModel orgListModel = this.converFromOrgDO(organizationDO);
            return orgListModel;
        }).collect(Collectors.toList());
        return Result.OK(organizationListFilterd).build();
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

    //根据机构姓名获取机构列表
    @RequestMapping(value = "/organizationByName",method = GET)
    public Result getOrgFromName(@RequestParam("organizationName")String orgName){
        List orgByName = organizationService.getOrgByName(orgName);
        int orgNum = orgByName.size();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("organizations",orgByName);
        jsonObject.put("organizationNum",orgNum);
        return Result.OK(jsonObject).build();
    }



    private OrgListModel converFromOrgDO(OrganizationDO organizationDO){
        OrgListModel orgListModel = new OrgListModel();
        BeanUtils.copyProperties(organizationDO,orgListModel);
        return orgListModel;
    }
}
