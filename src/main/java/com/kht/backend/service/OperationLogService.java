package com.kht.backend.service;

import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.entity.Result;

public interface OperationLogService {

    public Result getAllOperationLogs(int pageNum);

    public Result getOperationLogsByEmployeeName(int pageNum, String employeeName);

    public Result getOperationLogsByLogTime(int pageNum, Long startTime, Long endTime);

    public boolean addOperationLog(OperaLogDO operaLogDO);

}
