package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.DataDictionaryModel;

import java.util.List;

public interface DataDictionaryService {

    /**
     * 获取所有数据字典信息
     * @return
     */
    public List<DataDictionaryModel> getAllDataDictionaries();

    /**
     * 根据字段名获取数据字典信息
     * @param colName
     * @param pageNum
     * @return
     */
    public Result getDataDictionariesByColName(String colName, int pageNum);

    /**
     * 获取单个字段的数据字典信息
     * @param colCode
     * @param tabCode
     * @return
     */
    public Result getColumnValues(String colCode, String tabCode);

    /**
     * 获取数据字典的所有字段信息
     * @return
     */
    public Result getAllColumns();

    /**
     * 添加数据字典的字段取值
     * @param mainCode
     * @param valueCode
     * @param value
     * @return
     * @throws ServiceException
     */
    public Result addDataDictionary(int mainCode, String valueCode, String value) throws ServiceException;

    /**
     * 删除数据字典的字段取值
     * @param subCode
     * @return
     */
    public Result removeDataDictionary(int subCode);

    /**
     * 修改数据字典的字段信息
     * @param subCode
     * @param valueCode
     * @param value
     * @return
     */
    public Result modifyDataDictionary(int subCode, String valueCode, String value);

}
