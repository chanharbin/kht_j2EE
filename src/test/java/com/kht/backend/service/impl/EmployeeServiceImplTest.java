package com.kht.backend.service.impl;

import com.kht.backend.App;
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
        Result employeeById = employeeService.getEmployeeById("123");
        System.out.println(employeeById.getData());
    }
}