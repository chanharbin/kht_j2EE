package com.kht.backend.service.impl;

import com.kht.backend.App;
import com.kht.backend.dao.UserDOMapper;
import com.kht.backend.dataobject.UserDO;
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
    @Autowired
    private UserDOMapper userDOMapper;
    @Test
    public void userRegister() {
        Result result=userService.userRegister(12345678912L,"1234","123456");
        System.out.println(result.getData());
    }

    @Test
    public void getOtp() {
        UserDO userDO=userDOMapper.selectByTelephone(12345678910L);
        System.out.println(userDO.getUserType());
        System.out.println(userDO.toString());
    }

    @Test
    public void getUserAccountInfo() {
        Result result=userService.userRegister(12345678910L,"0","654321");
    }

    @Test
    public void userLogin() {
        Result result=userService.userLogin(12345678912L,"654321");
        System.out.println(result.getData());
    }

    @Test
    public void getUserInfo() {
        Result result=userService.getUserInfo(2);
        System.out.println(result.getData());
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
        UserDO userDO=new UserDO();
        userDO.setUserCode(2);
        userDO.setTelephone(12345678912L);
        userDO.setPassword("654321");
        Result result=userService.modifyUserInfo(userDO);
        System.out.println(result.getData());
    }
}