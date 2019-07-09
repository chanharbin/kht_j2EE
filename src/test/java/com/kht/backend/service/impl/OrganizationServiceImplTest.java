/*
package com.kht.backend.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.kht.backend.App;
import com.kht.backend.dao.CustAcctDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.OrganizationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;


@SpringBootTest(classes = App.class)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class OrganizationServiceImplTest {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;


    @Test
    public void increaseOrganization() {
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrgCode("12");
        organizationDO.setOrgName("11");
        organizationDO.setOrgAddr("123");
        long telphone = 1311111;
        organizationDO.setOrgTel(telphone);
        Result result = organizationService.increaseOrganization(organizationDO);
        System.out.println(result.getData());
    }

    @Test
    public void decreaseOrganization() {
        Result result = organizationService.decreaseOrganization("123");
        System.out.println(result.getData());
    }

    @Test
    public void getOrganizationList() {
        Map organizationList = organizationService.getOrganizationList(10);
        String s = JSONObject.toJSONString(organizationList);
        System.out.println(s);


    }

    @Test
    public void modifyOrganizationInfo() {
        OrganizationDO organizationDO = new OrganizationDO();
        organizationDO.setOrgCode("12");
        organizationDO.setOrgName("111");
        organizationDO.setOrgAddr("123");
        Result result = organizationService.modifyOrganizationInfo(organizationDO);
        System.out.println(result.getData());
    }

 */
/*   @Test
    public void getOrganizationUser() {
        Result organizationUser = organizationService.getOrganizationUser("12", 10,true);
        String s = JSONObject.toJSONString(organizationUser.getData());
        System.out.println(s);

    }*//*


    @Test
    public void getOrganizationById() {
        Result re = organizationService.getOrganizationById("1");
        String s = JSONObject.toJSONString(re.getData());
        System.out.println(s);
    }
    @Test
    public void getTodayCount(){
        int todayCount = custAcctDOMapper.getTodayCount((long) 20001010,"1000");
        System.out.println(todayCount);
    }
    @Test
    public void getTotalUserNum(){
        int allCount = custAcctDOMapper.getAllCount("1000");
        System.out.println(allCount);
    }
}*/
