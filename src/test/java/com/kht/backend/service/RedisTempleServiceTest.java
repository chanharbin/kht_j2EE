package com.kht.backend.service;

import com.kht.backend.App;
import com.kht.backend.service.model.MenuModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = App.class)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class RedisTempleServiceTest {
    @Autowired
    RedisTempleService redisTempleService;
    @Test
    public void redisTest(){
        MenuModel menuModel = new MenuModel();
        menuModel.setId(1);
        menuModel.setAuthority(1);
        menuModel.setOperation(1);
        redisTempleService.set("key",menuModel);

        MenuModel menuModel1 = redisTempleService.get("key",MenuModel.class);

        System.out.println(menuModel.getId());

    }




}