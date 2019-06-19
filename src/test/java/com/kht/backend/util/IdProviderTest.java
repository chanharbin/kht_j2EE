package com.kht.backend.util;

import com.kht.backend.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import  com.kht.backend.util.IdProvider;

import static org.junit.Assert.*;
@SpringBootTest(classes = App.class)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class IdProviderTest {
    @Autowired
    IdProvider idProvider;
    @Test
    public void getId() {
        for(int i=0;i<5;i++) {
            System.out.println(idProvider.getId("fff"));
        }
    }

    @Test
    public void nextId() {
        /*for(int i=0;i<10;i++) {
            System.out.println(idProvider.nextId());
        }*/

        String username="FFF013543GHJ";
        System.out.println(Long.parseLong(username));
    }

    @Test
    public void timeGen() {
        for(int i=0;i<10;i++) {
            System.out.println(idProvider.timeGen());
        }
    }
}