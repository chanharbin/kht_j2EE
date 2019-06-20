package com.kht.backend.service.impl;

import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.SubDataDictDO;

import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataDictionaryServiceImplTest {

    @Autowired
    private DataDictionaryServiceImpl dataDictionaryService;

    @Test
    public void getAllDataDictionariesTest() {
        for (int i = 0; i < 3; i++) {
            System.out.println(dataDictionaryService.getAllDataDictionaries(i));
        }
    }

    @Test
    public void addDataDictionaryTest() {
    }

    @Test
    public void removeDataDictionaryTest() {
    }

    @Test
    public void modifyDataDictionaryTest() {
    }
}