package com.kht.backend.service.impl;

import com.github.pagehelper.Page;
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
        for (int i = 1; i <= 5; i++) {
            Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getAllDataDictionaries(i).getData();
            Page<DataDictionaryModel> page = (Page<DataDictionaryModel>) resultData.get("data");
            List<DataDictionaryModel> dataDictionaryModelList = (List<DataDictionaryModel>) page.getResult();
            int size = dataDictionaryModelList.size();
            System.out.println(size);
            for (int j = 0; j < size; j++) {
                System.out.println(dataDictionaryModelList.get(j));
            }
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