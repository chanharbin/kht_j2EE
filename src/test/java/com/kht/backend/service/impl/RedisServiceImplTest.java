package com.kht.backend.service.impl;

import com.kht.backend.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest(classes = App.class)
@RunWith(SpringRunner.class)
public class RedisServiceImplTest {
    @Autowired
    private RedisServiceImpl redisService;
    @Test
    public void getOrganizationName() {
    }

    @Test
    public void getDataDictionary() {
    }

    @Test
    public void getSystemParameterList() {
        System.out.println(redisService.getSystemParameterList());
    }

    @Test
    public void getParaValue() {
        System.out.println(redisService.getParaValue("pageSize"));
    }

    @Test
    public void getPosionName(){
        System.out.println(redisService.getPosName(2));
    }
}