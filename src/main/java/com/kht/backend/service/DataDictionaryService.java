package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.DataDictionaryModel;

import java.util.List;

public interface DataDictionaryService {

    public List<DataDictionaryModel> getAllDataDictionaries();

    public Result getDataDictionariesByColName(String colName, int pageNum);

    public Result getColumnValues(String colCode, String tabCode);

    public Result getAllColumns();

    public Result addDataDictionary(int mainCode, String valueCode, String value) throws ServiceException;

    public Result removeDataDictionary(int subCode);

    public Result modifyDataDictionary(int subCode, String valueCode, String value);

}
