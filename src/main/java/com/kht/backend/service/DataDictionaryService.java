package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

public interface DataDictionaryService {

    public Result getAllDataDictionaries(int pageNum);

    public Result getDataDictionariesByColName(String colName, int pageNum);

    public Result getColumnValues(String colCode, String tabCode);

    public Result addDataDictionary(int mainCode, String valueCode, String value) throws ServiceException;

    public Result removeDataDictionary(int subCode);

    public Result modifyDataDictionary(int subCode, String valueCode, String value);

}
