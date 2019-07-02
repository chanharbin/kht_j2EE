package com.kht.backend.controller;

import com.alibaba.fastjson.JSONObject;
import com.kht.backend.aspect.MethodLog;
import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.model.OrgListModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
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
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private ValueOperations<String,Object> valueOperations;
    /*
    * 获取机构列表
    * */
    @MethodLog(22)
    @RequestMapping(value = "/organization",method = GET)
    public Result getOrganization(@RequestParam("page_num") int pageNum){
        Map orgResult = organizationService.getOrganizationList(pageNum);
        return Result.OK(orgResult).build();
    }
    @RequestMapping(value = "/organization/list",method = GET)
    public Result getOrganization(){
        List<OrganizationDO> organizationList = (List<OrganizationDO>)valueOperations.get("OrganizationList");
        if(organizationList.isEmpty()){
            organizationList = organizationDOMapper.selectAll();
        }
        List organizationListFilterd = organizationList.stream().map(organizationDO -> {
            OrgListModel orgListModel = this.converFromOrgDO(organizationDO);
            return orgListModel;
        }).collect(Collectors.toList());
        return Result.OK(organizationListFilterd).build();
    }

    //新增机构
    @MethodLog(23)
    @RequestMapping(value = "/organization", method = POST)
    public Result increaseOrg(@RequestParam("orgName") String orgName,
                              @RequestParam("orgCode") String orgCode,
                              @RequestParam("orgTel") Long orgtel,
                              @RequestParam("orgAddr") String orgAddr){
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrgTel(orgtel);
        organizationDO.setOrgAddr(orgAddr);
        organizationDO.setOrgName(orgName);
        organizationDO.setOrgCode(orgCode);
        Result result = organizationService.increaseOrganization(organizationDO);
        return result;
    }
    //修改机构
    @MethodLog(24)
    @RequestMapping(value = "/organization",method = PUT)
    public Result modifyOrg(@RequestParam("orgName") String orgName,
                            @RequestParam("orgCode") String orgCode,
                            @RequestParam("orgTel") Long orgtel,
                            @RequestParam("orgAddr") String orgAddr){
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrgTel(orgtel);
        organizationDO.setOrgAddr(orgAddr);
        organizationDO.setOrgName(orgName);
        organizationDO.setOrgCode(orgCode);
        Result result = organizationService.modifyOrganizationInfo(organizationDO);
        return result;
    }
    //删除机构
    @MethodLog(25)
    @RequestMapping(value = "/organization",method = DELETE)
    public Result deleteOrg(@RequestParam("orgCode")String orgCode){
        Result result = organizationService.decreaseOrganization(orgCode);
        return result;
    }

    //根据机构姓名获取机构列表
    @MethodLog(26)
    @RequestMapping(value = "/organization/name",method = GET)
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
