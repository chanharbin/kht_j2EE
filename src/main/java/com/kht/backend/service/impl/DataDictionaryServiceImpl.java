package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.MainDataDictDOMapper;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.MainDataDictDO;
import com.kht.backend.dataobject.SubDataDictDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.DataDictionaryService;
import com.kht.backend.service.model.ColumnModel;
import com.kht.backend.service.model.ColumnValueModel;
import com.kht.backend.service.model.DataDictionaryModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DataDictionaryServiceImpl implements DataDictionaryService{

    @Autowired
    private SubDataDictDOMapper subDataDictDOMapper;

    @Autowired
    private MainDataDictDOMapper mainDataDictDOMapper;

    @Autowired
    private RedisServiceImpl redisService;

    //private int pageSize = Integer.parseInt(redisService.getSystemParameterList().get(0).getParaValue());
    private int pageSize = 10;

    @Override
    public List<DataDictionaryModel> getAllDataDictionaries() {
        return subDataDictDOMapper.listAll();
    }

    @Override
    public Result getDataDictionariesByColName(String colName, int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<DataDictionaryModel> dataDictionaryModelList = subDataDictDOMapper.selectByColName(colName);
        PageInfo<DataDictionaryModel> page = new PageInfo<>(dataDictionaryModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    @Override
    public Result getColumnValues(String colCode, String tabCode) {
        List<ColumnValueModel> columnValueModelList = subDataDictDOMapper.selectColumnValues(colCode, tabCode);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("data", columnValueModelList);
        return  Result.OK(resultData).build();
    }

    @Override
    public Result getAllColumns() {
        List<MainDataDictDO> mainDataDictDOList = mainDataDictDOMapper.listAll();
        List<ColumnModel> columnModelList = mainDataDictDOList.stream().map(mainDataDictDO -> {
            ColumnModel columnModel = new ColumnModel();
            BeanUtils.copyProperties(mainDataDictDO, columnModel);
            return columnModel;
        }).collect(Collectors.toList());
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("data", columnModelList);
        return  Result.OK(resultData).build();
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
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "添加数据字典信息失败");
        }
        int subCode = subDataDictDOMapper.selectSubCode(mainCode, valueCode);
        return Result.OK(subCode).build();
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
