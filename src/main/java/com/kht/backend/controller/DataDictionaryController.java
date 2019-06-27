package com.kht.backend.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.aop.annotation.MethodLog;
import com.kht.backend.entity.*;
import com.kht.backend.service.DataDictionaryService;
import com.kht.backend.service.model.DataDictionaryModel;
import org.springframework.beans.factory.annotation.*;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class DataDictionaryController {

    @Autowired
    private DataDictionaryService dataDictionaryService;

    @Value("${app.pageSize}")
    private int pageSize;

    @RequestMapping(value = "/data-dictionary", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllDataDictionaries(@RequestParam("pageNum") int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<DataDictionaryModel> dataDictionaryModelList = dataDictionaryService.getAllDataDictionaries();
        PageInfo<DataDictionaryModel> page = new PageInfo<>(dataDictionaryModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    @RequestMapping(value = "/data-dictionary/search", method = GET, produces = "application/json;charset=UTF-8")
    public Result getDataDictionariesByColName(@RequestParam("colName") String colName, @RequestParam("pageNum") int pageNum) {
        if (colName == null || colName.trim().isEmpty())
            return this.getAllDataDictionaries(1);
        return dataDictionaryService.getDataDictionariesByColName(colName.trim(), pageNum);
    }

    @RequestMapping(value = "/data-dictionary/{tabCode}/{colCode}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getColumnValues(@PathVariable("colCode") String colCode, @PathVariable("tabCode") String tabCode) {
        return dataDictionaryService.getColumnValues(colCode, tabCode);
    }

    @RequestMapping(value = "/data-dictionary/columns", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllColumns() {
        return dataDictionaryService.getAllColumns();
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
