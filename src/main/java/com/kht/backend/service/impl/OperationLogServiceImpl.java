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

    public OperationLogServiceImpl(RedisServiceImpl redisService) {
        String paraValue = redisService.getParaValue("pageSize");
        try {
            pageSize = Integer.parseInt(paraValue);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private int pageSize;

    /**
     * 获取所有操作记录
     * @param pageNum
     * @return
     */
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

    /**
     * 根据操作人员获取操作记录
     * @param pageNum
     * @param employeeName
     * @return
     */
    @Override
    public Result getOperationLogsByEmployeeName(int pageNum, String employeeName) {
        PageHelper.startPage(pageNum,pageSize);
        List<OperationLogModel> operationLogModelList = operaLogDOMapper.selectByEmployeeName(employeeName);
        PageInfo<OperationLogModel> page = new PageInfo<>(operationLogModelList);
        Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    /**
     * 根据时间段获取操作记录
     * @param pageNum
     * @param startTime
     * @param endTime
     * @return
     */
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

    /**
     * 添加操作记录
     * @param operaLogDO
     * @return
     */
    @Override
    public boolean addOperationLog(OperaLogDO operaLogDO) {
        int affectedRow = operaLogDOMapper.insert(operaLogDO);
        return affectedRow > 0;
    }
}
