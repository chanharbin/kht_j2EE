package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.OperaLogDOMapper;
import com.kht.backend.entity.Result;
import com.kht.backend.service.OperationLogService;
import com.kht.backend.service.model.OperationLogModel;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.List;
/*
@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperaLogDOMapper operaLogDOMapper;

    @Value("${app.pageSize}")
    private int pageSize;

    @Override
    public Result getOperationLogsByTime(int pageNum, Long startTime, Long endTime) {
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLogModel> operationLogModelList = operaLogDOMapper.listAll();
        PageInfo<DataDictionaryModel> page = new PageInfo<>(dataDictionaryModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }
}

 */