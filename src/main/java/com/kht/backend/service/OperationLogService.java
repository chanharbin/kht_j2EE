package com.kht.backend.service;

import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.entity.Result;

public interface OperationLogService {
    //记录操作记录
    public Result increseOperationLog(OperaLogDO operaLogDO);
    //获取所有的操作记录列表
    public Result getOperationLogList(int pageNum);
    //获取指定人员的操作记录
    public Result getOperationLogByCode(String code,int pageNum);
    //获取某个时间段之间的操作记录
    public Result getOperationLogBetweenTime(String startTime,String endTime,int pageNum);
}
