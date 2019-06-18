package com.kht.backend.service.impl;

import com.kht.backend.App;
import com.kht.backend.dao.MainDateDictDOMapper;
import com.kht.backend.dataobject.MainDateDictDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = App.class)
// 让 JUnit 运行 Spring 的测试环境， 获得 Spring 环境的上下文的支持
@RunWith(SpringRunner.class)
public class MainDataTest {
    @Autowired
    private MainDateDictDOMapper mainDateDictDOMapper;
    @Test
    public void Test(){
        MainDateDictDO mainDateDictDO = new MainDateDictDO();
        mainDateDictDO.setColCode("12");
        mainDateDictDO.setColName("12");
        mainDateDictDO.setTabCode("1233");
        int i = mainDateDictDOMapper.insertSelective(mainDateDictDO);
        System.out.println(mainDateDictDO.getMainCode());
        System.out.println(i);

    }
}
