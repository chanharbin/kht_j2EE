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

    public DataDictionaryServiceImpl(RedisServiceImpl redisService) {
        String paraValue = redisService.getParaValue("pageSize");
        try {
            pageSize = Integer.parseInt(paraValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int pageSize;

    @Override
    public List<DataDictionaryModel> getAllDataDictionaries() {
        return subDataDictDOMapper.listAll();
    }

    /**
     * 根据字段名获取数据字典信息
     * @param colName
     * @param pageNum
     * @return
     */
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

    /**
     *获取单个字段的数据字典信息
     * @param colCode
     * @param tabCode
     * @return
     */
    @Override
    public Result getColumnValues(String colCode, String tabCode) {
        List<ColumnValueModel> columnValueModelList = subDataDictDOMapper.selectColumnValues(colCode, tabCode);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("data", columnValueModelList);
        return  Result.OK(resultData).build();
    }

    /**
     * 获取数据字典的所有字段信息
     * @return
     */
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

    /**
     * 添加数据字典的字段取值
     * @param mainCode
     * @param valueCode
     * @param value
     * @return
     * @throws ServiceException
     */
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

    /**
     * 删除数据字典的字段取值
     * @param subCode
     * @return
     */
    @Transactional
    @Override
    public Result removeDataDictionary(int subCode) {
        int affectedRow = subDataDictDOMapper.deleteByPrimaryKey(subCode);
        if(affectedRow <= 0){
            throw new ServiceException(ErrorCode. SERVER_EXCEPTION, "删除数据字典信息失败");
        }
        return Result.OK("删除数据字典信息成功").build();
    }

    /**
     * 修改数据字典的字段取值
     * @param subCode
     * @param valueCode
     * @param value
     * @return
     */
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
