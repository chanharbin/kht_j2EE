package com.kht.backend.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.SubDataDictDO;

import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.ColumnValueModel;
import com.kht.backend.service.model.DataDictionaryModel;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
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
            List<DataDictionaryModel> dataDictionaryModelList = page.getResult();
            int size = dataDictionaryModelList.size();
            System.out.println(size);
            for (int j = 0; j < size; j++) {
                System.out.println(dataDictionaryModelList.get(j));
            }
        }
    }

    @Test
    public void getDataDictionariesByColNameTest() {
        Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getDataDictionariesByColName("性别", 1).getData();
        Page<DataDictionaryModel> page = (Page<DataDictionaryModel>) resultData.get("data");
        List<DataDictionaryModel> dataDictionaryModelList = page.getResult();
        int size = dataDictionaryModelList.size();
        System.out.println(size);
        for (int j = 0; j < size; j++) {
            System.out.println(dataDictionaryModelList.get(j));
        }
    }

    @Test
    public void getColumnValuesTest() {
        Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getColumnValues("GENDER", "acct_open_info").getData();
        List<ColumnValueModel> columnValueModelList1 = (List<ColumnValueModel>) resultData.get("data");
        List<ColumnValueModel> columnValueModelList2 = new ArrayList<>();
        ColumnValueModel columnValueModel = new ColumnValueModel();
        columnValueModel.setValueCode("0");
        columnValueModel.setValue("男");
        columnValueModelList2.add(columnValueModel.clone());
        columnValueModel.setValueCode("1");
        columnValueModel.setValue("女");
        columnValueModelList2.add(columnValueModel.clone());
        Assert.assertEquals(columnValueModelList2, columnValueModelList1);
    }

    @Test
    public void addDataDictionaryTest() {
        try {
            int subCode = (int) dataDictionaryService.addDataDictionary(1, "2", "变性人").getData();
            Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getColumnValues("GENDER", "acct_open_info").getData();
            List<ColumnValueModel> columnValueModelList1 = (List<ColumnValueModel>) resultData.get("data");
            List<ColumnValueModel> columnValueModelList2 = new ArrayList<>();
            ColumnValueModel columnValueModel = new ColumnValueModel();
            columnValueModel.setValueCode("0");
            columnValueModel.setValue("男");
            columnValueModelList2.add(columnValueModel.clone());
            columnValueModel.setValueCode("1");
            columnValueModel.setValue("女");
            columnValueModelList2.add(columnValueModel.clone());
            columnValueModel.setValueCode("2");
            columnValueModel.setValue("变性人");
            columnValueModelList2.add(columnValueModel.clone());
            Assert.assertEquals(columnValueModelList2, columnValueModelList1);
            dataDictionaryService.removeDataDictionary(subCode);
        }
        catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void removeDataDictionaryTest() {
        try {
            Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getColumnValues("GENDER", "acct_open_info").getData();
            List<ColumnValueModel> columnValueModelList1 = (List<ColumnValueModel>) resultData.get("data");
            int subCode = (int) dataDictionaryService.addDataDictionary(1, "2", "变性人").getData();
            dataDictionaryService.removeDataDictionary(subCode);
            resultData = (Map<String, Object>) dataDictionaryService.getColumnValues("GENDER", "acct_open_info").getData();
            List<ColumnValueModel> columnValueModelList2 = (List<ColumnValueModel>) resultData.get("data");
            Assert.assertEquals(columnValueModelList1, columnValueModelList2);
        }
        catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void modifyDataDictionaryTest() {
        try {
            int subCode = (int) dataDictionaryService.addDataDictionary(1, "2", "人妖").getData();
            dataDictionaryService.modifyDataDictionary(subCode, "2", "变性人");
            Map<String, Object> resultData = (Map<String, Object>) dataDictionaryService.getColumnValues("GENDER", "acct_open_info").getData();
            List<ColumnValueModel> columnValueModelList1 = (List<ColumnValueModel>) resultData.get("data");
            List<ColumnValueModel> columnValueModelList2 = new ArrayList<>();
            ColumnValueModel columnValueModel = new ColumnValueModel();
            columnValueModel.setValueCode("0");
            columnValueModel.setValue("男");
            columnValueModelList2.add(columnValueModel.clone());
            columnValueModel.setValueCode("1");
            columnValueModel.setValue("女");
            columnValueModelList2.add(columnValueModel.clone());
            columnValueModel.setValueCode("2");
            columnValueModel.setValue("变性人");
            columnValueModelList2.add(columnValueModel.clone());
            Assert.assertEquals(columnValueModelList2, columnValueModelList1);
            dataDictionaryService.removeDataDictionary(subCode);
        }
        catch (ServiceException e) {
            e.printStackTrace();
        }
    }
}