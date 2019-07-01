package com.kht.backend.service.impl;

import com.kht.backend.App;
import com.kht.backend.dao.SysParaDOMapper;
import com.kht.backend.dao.UserDOMapper;
import com.kht.backend.dataobject.SysParaDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.impl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.SQLOutput;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;
@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SysParaDOMapper sysParaDOMapper;
    @Test
    public void userRegister() {

    }

    @Test
    public void getOtp() {
    }

    @Test
    public void getUserAccountInfo(){
       // System.out.println(userService.getAccountOpeningInfo(1).getData());
    }

    @Test
    public void userLogin() {
        List<SysParaDO> allSystemParameters = sysParaDOMapper.listAll();
    }

    @Test
    public void getUserInfo() {
        //Result result=userService.getUserInfo(2);
        //System.out.println(result.getData());
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
        //Result result=userService.modifyUserInfo(userDO);
        //System.out.println(result.getData());
    }

    @Test
    public void getAllDataInfoList() {
        //System.out.println(userService.getBrithDayFromIdCode("410322199202152910"));
    }
}