package com.kht.backend.service.impl;

import com.kht.backend.App;
import com.kht.backend.entity.Result;
import com.kht.backend.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Test
    public void userRegister() {
        Result result=userService.userRegister(12345678912L,"1234","123456");
        System.out.println(result.getData());
    }

    @Test
    public void getOtp() {
    }

    @Test
    public void getUserAccountInfo() {
    }

    @Test
    public void userLogin() {
    }

    @Test
    public void getUserInfo() {
    }

    @Test
    public void increaseAccountOpenInfo() {
    }

    @Test
    public void increaseCapitalAccount() {
    }

    @Test
    public void modifyCapitalAccountPassword() {
    }

    @Test
    public void getState() {
    }

    @Test
    public void modifyUserInfo() {
    }
}