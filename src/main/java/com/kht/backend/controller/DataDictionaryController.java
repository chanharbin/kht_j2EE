package com.kht.backend.controller;

import com.kht.backend.entity.*;
import com.kht.backend.service.DataDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @RequestMapping(value = "/data-dictionary", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllDataDictionaries(@RequestParam("pageNum") int pageNum) {
        return dataDictionaryService.getAllDataDictionaries(pageNum);
    }

    @RequestMapping(value = "/data-dictionary/{colName}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getDataDictionariesByColName(@RequestParam("colName") String colName, @RequestParam("pageNum") int pageNum) {
        return dataDictionaryService.getDataDictionariesByColName(colName, pageNum);
    }

    @RequestMapping(value = "/data-dictionary/column", method = GET, produces = "application/json;charset=UTF-8")
    public Result getColumnValues(@RequestParam("colCode") String colCode, @RequestParam("tabCode") String tabCode) {
        return dataDictionaryService.getColumnValues(colCode, tabCode);
    }

    @RequestMapping(value = "/data-dictionary", method = POST, produces = "application/json;charset=UTF-8")
    public Result addDataDictionary(@RequestParam("mainCode") int mainCode, @RequestParam("valueCode") String valueCode, @RequestParam("value") String value) {
        return dataDictionaryService.addDataDictionary(mainCode, valueCode, value);
    }

    @RequestMapping(value = "/data-dictionary", method = DELETE, produces = "application/json;charset=UTF-8")
    public Result removeDataDictionary(@RequestParam("subCode") int subCode) {
        return dataDictionaryService.removeDataDictionary(subCode);
    }

    @RequestMapping(value = "/data-dictionary", method = PUT, produces = "application/json;charset=UTF-8")
    public Result modifyDataDictionary(@RequestParam("subCode") int subCode, @RequestParam("valueCode") String valueCode, @RequestParam("value") String value) {
        return dataDictionaryService.modifyDataDictionary(subCode, valueCode, value);
    }

}
