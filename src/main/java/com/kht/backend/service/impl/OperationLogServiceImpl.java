package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.OperaLogDOMapper;
import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.OperationLogService;
import com.kht.backend.service.model.OperationLogModel;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class OperationLogServiceImpl implements OperationLogService {

    @Autowired
    private OperaLogDOMapper operaLogDOMapper;

    @Autowired
    private RedisServiceImpl redisService;

    //private int pageSize = Integer.parseInt(redisService.getSystemParameterList().get(0).getParaValue());
    private int pageSize = 10;

    @Override
    public Result getAllOperationLogs(int pageNum) {
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLogModel> operationLogModelList = operaLogDOMapper.listAll();
        PageInfo<OperationLogModel> page = new PageInfo<>(operationLogModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    @Override
    public Result getOperationLogsByEmployeeCode(int pageNum, String employeeCode) {
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLogModel> operationLogModelList = operaLogDOMapper.selectByEmployeeCode(employeeCode);
        PageInfo<OperationLogModel> page = new PageInfo<>(operationLogModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    @Override
    public Result getOperationLogsByLogTime(int pageNum, Long startTime, Long endTime) {
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLogModel> operationLogModelList = operaLogDOMapper.selectByLogTime(startTime, endTime);
        PageInfo<OperationLogModel> page = new PageInfo<>(operationLogModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    @Override
    public boolean addOperationLog(OperaLogDO operaLogDO) {
        int affectedRow = operaLogDOMapper.insert(operaLogDO);
        return affectedRow > 0;
    }
}
