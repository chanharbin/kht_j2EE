package com.kht.backend.service.impl;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

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
}