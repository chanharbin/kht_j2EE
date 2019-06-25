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

    @RequestMapping(value = "/data_dict", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllDataDictionaries(@RequestParam("pageNum") int pageNum) {
        return dataDictionaryService.getAllDataDictionaries(pageNum);
    }

    @RequestMapping(value = "/data_dict/column", method = GET, produces = "application/json;charset=UTF-8")
    public Result getColumnValues(@RequestParam("colCode") String colCode, @RequestParam("tabCode") String tabCode) {
        return dataDictionaryService.getColumnValues(colCode, tabCode);
    }

    @RequestMapping(value = "/data_dict", method = POST, produces = "application/json;charset=UTF-8")
    public Result addDataDictionary(@RequestParam("mainCode") int mainCode, @RequestParam("valueCode") String valueCode, @RequestParam("value") String value) {
        return dataDictionaryService.addDataDictionary(mainCode, valueCode, value);
    }

    @RequestMapping(value = "/data_dict", method = DELETE, produces = "application/json;charset=UTF-8")
    public Result removeDataDictionary(@RequestParam("subCode") int subCode) {
        return dataDictionaryService.removeDataDictionary(subCode);
    }

    @RequestMapping(value = "/data_dict", method = PUT, produces = "application/json;charset=UTF-8")
    public Result modifyDataDictionary(@RequestParam("subCode") int subCode, @RequestParam("valueCode") String valueCode, @RequestParam("value") String value) {
        return dataDictionaryService.modifyDataDictionary(subCode, valueCode, value);
    }

}
