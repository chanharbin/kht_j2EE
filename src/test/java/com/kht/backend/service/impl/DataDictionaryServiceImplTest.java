package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.SubDataDictDO;

import com.kht.backend.service.model.DataDictionaryModel;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataDictionaryServiceImplTest {

    @Autowired
    private DataDictionaryServiceImpl dataDictionaryService;

    @Test
    public void getAllDataDictionariesTest() {
        for (int i = 1; i < 4; i++) {
            Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getAllDataDictionaries(i).getData();
            PageInfo<DataDictionaryModel> page = (PageInfo<DataDictionaryModel>) resultData.get("data");
            List<DataDictionaryModel> dataDictionaryModelList = page.getList();
            System.out.println(dataDictionaryModelList);
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