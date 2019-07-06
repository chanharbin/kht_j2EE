package com.kht.backend.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.aspect.MethodLog;
import com.kht.backend.entity.*;
import com.kht.backend.service.DataDictionaryService;
import com.kht.backend.service.impl.RedisServiceImpl;
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

    @Autowired
    private RedisServiceImpl redisService;

    public DataDictionaryController(RedisServiceImpl redisService) {
        String paraValue = redisService.getParaValue("pageSize");
        try {
            pageSize = Integer.parseInt(paraValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int pageSize;

    /**
     * 获取所有数据字典信息
     * @param pageNum
     * @return
     */
    @MethodLog(13)
    @RequestMapping(value = "/data-dictionary", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllDataDictionaries(@RequestParam("pageNum") int pageNum) {
        try {
            pageSize = Integer.parseInt(redisService.getParaValue("pageSize"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        PageHelper.startPage(pageNum, pageSize);
        List<DataDictionaryModel> dataDictionaryModelList = dataDictionaryService.getAllDataDictionaries();
        PageInfo<DataDictionaryModel> page = new PageInfo<>(dataDictionaryModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        resultData.put("pageSize", pageSize);
        return Result.OK(resultData).build();
    }

    /**
     * 根据字段名获取数据字典信息
     * @param colName
     * @param pageNum
     * @return
     */
    @MethodLog(14)
    @RequestMapping(value = "/data-dictionary/column", method = GET, produces = "application/json;charset=UTF-8")
    public Result getDataDictionariesByColName(@RequestParam("colName") String colName, @RequestParam("pageNum") int pageNum) {
        if (colName == null || colName.trim().isEmpty())
            return this.getAllDataDictionaries(1);
        return dataDictionaryService.getDataDictionariesByColName(colName.trim(), pageNum);
    }

    /**
     * 弃用
     * 获取单个字段的数据字典信息
     * @param colCode
     * @param tabCode
     * @return
     */
    /*
    @RequestMapping(value = "/data-dictionary/{tabCode}/{colCode}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getColumnValues(@PathVariable("colCode") String colCode, @PathVariable("tabCode") String tabCode) {
        return dataDictionaryService.getColumnValues(colCode, tabCode);
    }
    */

    /**
     * 获取数据字典的所有字段信息
     * @return
     */
    @RequestMapping(value = "/data-dictionary/columns", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllColumns() {
        return dataDictionaryService.getAllColumns();
    }

    /**
     * 添加数据字典字段取值信息
     * @param mainCode
     * @param valueCode
     * @param value
     * @return
     */
    @MethodLog(16)
    @RequestMapping(value = "/data-dictionary", method = POST, produces = "application/json;charset=UTF-8")
    public Result addDataDictionary(@RequestParam("mainCode") int mainCode, @RequestParam("valueCode") String valueCode, @RequestParam("value") String value) {
        return dataDictionaryService.addDataDictionary(mainCode, valueCode, value);
    }

    /**
     * 删除数据字典字段取值信息
     * @param subCode
     * @return
     */
    @MethodLog(18)
    @RequestMapping(value = "/data-dictionary", method = DELETE, produces = "application/json;charset=UTF-8")
    public Result removeDataDictionary(@RequestParam("subCode") int subCode) {
        return dataDictionaryService.removeDataDictionary(subCode);
    }

    /**
     * 修改数据字典字段取值信息
     * @param subCode
     * @param valueCode
     * @param value
     * @return
     */
    @MethodLog(17)
    @RequestMapping(value = "/data-dictionary", method = PUT, produces = "application/json;charset=UTF-8")
    public Result modifyDataDictionary(@RequestParam("subCode") int subCode, @RequestParam("valueCode") String valueCode, @RequestParam("value") String value) {
        return dataDictionaryService.modifyDataDictionary(subCode, valueCode, value);
    }

}
