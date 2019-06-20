package com.kht.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kht.backend.App;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.EmployeeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = App.class)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class EmployeeServiceImplTest {
    @Autowired
    private EmployeeService employeeService;

    @Test
    public void getEmployeeById() {
        Result employeeById = employeeService.getEmployeeById("0");
        String resultStr = JSONObject.toJSONString(employeeById.getData());
        System.out.println(resultStr);
    }

    @Test
    public void deleteEmployee() {
        Result result = employeeService.deleteEmployee("123");
        System.out.println(result.getMsg());
    }

    @Test
    public void increaseEmployee() {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeCode("0");
        employeeDO.setUserCode(1);
        employeeDO.setAddress("123");
        employeeDO.setEmail("12345");
        employeeDO.setEmployeeName("123");
        employeeDO.setEmployeeStatus("1");
        employeeDO.setIdCode("414");
        employeeDO.setPosition("1");
        long telephone = 12345213;
        employeeDO.setTelephone(telephone);
        employeeService.increaseEmployee(employeeDO,"org");
        System.out.println(employeeDO.getEmployeeCode());
    }


    @Test
    public void modifyEmployee() {
        EmployeeDO employeeDO = new EmployeeDO();
        employeeDO.setEmployeeCode("org0176H3AQ");
        employeeDO.setUserCode(1);
        employeeDO.setAddress("123");
        employeeDO.setEmail("12345");
        employeeDO.setEmployeeName("123");
        employeeDO.setEmployeeStatus("1");
        employeeDO.setIdCode("414");
        employeeDO.setPosition("1");
        long telephone = 1340000;
        employeeDO.setTelephone(telephone);
        Result result = employeeService.modifyEmployee(employeeDO);
        System.out.println(result.getMsg());
    }

    @Test
    public void getEmployeeByName() {
        String name = "123";
        Result employeeByName = employeeService.getEmployeeByName(name);
        String s = JSONObject.toJSONString(employeeByName.getData());
        System.out.println(s);
    }

    @Test
    public void listEmployee() {
        Result result = employeeService.listEmployee(10);
        String s = JSONObject.toJSONString(result.getData());
        System.out.println(s);
    }
}