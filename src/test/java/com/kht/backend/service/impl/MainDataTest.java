package com.kht.backend.service.impl;

import com.kht.backend.App;
import com.kht.backend.dao.MainDataDictDOMapper;
import com.kht.backend.dataobject.MainDataDictDO;
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
    private MainDataDictDOMapper mainDataDictDOMapper;
    @Test
    public void Test(){
        MainDataDictDO mainDataDictDO = new MainDataDictDO();
        mainDataDictDO.setColCode("12");
        mainDataDictDO.setColName("12");
        mainDataDictDO.setTabCode("1233");
        int i = mainDataDictDOMapper.insertSelective(mainDataDictDO);
        System.out.println(mainDataDictDO.getMainCode());
        System.out.println(i);

    }
}
