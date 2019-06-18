package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.SubDataDictDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.DataDictionaryService;
import com.kht.backend.service.model.DataDictionaryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{

    @Autowired
    private SubDataDictDOMapper subDataDictDOMapper;

    @Override
    public Result getAllDataDictionaries(int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<DataDictionaryModel> dataDictionaryModelList = subDataDictDOMapper.listAll();
        PageInfo<DataDictionaryModel> page = new PageInfo<>(dataDictionaryModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    @Transactional
    @Override
    public Result addDataDictionary(int mainCode, String valueCode, String value) throws ServiceException {
        SubDataDictDO subDataDictDO = new SubDataDictDO();
        subDataDictDO.setMainCode(mainCode);
        subDataDictDO.setValueCode(valueCode);
        subDataDictDO.setValue(value);
        int affectedRow = subDataDictDOMapper.insertSelective(subDataDictDO);
        if(affectedRow <= 0){
            throw new ServiceException(ErrorCode. SERVER_EXCEPTION, "添加数据字典信息失败");
        }
        return Result.OK("添加数据字典信息成功").build();
    }

    @Transactional
    @Override
    public Result removeDataDictionary(int subCode) {
        int affectedRow = subDataDictDOMapper.deleteByPrimaryKey(subCode);
        if(affectedRow <= 0){
            throw new ServiceException(ErrorCode. SERVER_EXCEPTION, "删除数据字典信息失败");
        }
        return Result.OK("删除数据字典信息成功").build();
    }

    @Transactional
    @Override
    public Result modifyDataDictionary(int subCode, String valueCode, String value) {
        SubDataDictDO subDataDictDO = new SubDataDictDO();
        subDataDictDO.setSubCode(subCode);
        subDataDictDO.setValueCode(valueCode);
        subDataDictDO.setValue(value);
        int affectedRow = subDataDictDOMapper.updateByPrimaryKeySelective(subDataDictDO);
        if(affectedRow <= 0){
            throw new ServiceException(ErrorCode. SERVER_EXCEPTION, "修改数据字典信息失败");
        }
        return Result.OK("修改数据字典信息成功").build();
    }
}
