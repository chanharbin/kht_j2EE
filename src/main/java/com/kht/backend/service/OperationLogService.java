package com.kht.backend.service;

import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

public interface OperationLogService {

    public Result getAllOperationLogs(int pageNum);

    public Result getOperationLogsByEmployeeCode(int pageNum, String employeeCode);

    public Result getOperationLogsByLogTime(int pageNum, Long startTime, Long endTime);

    public boolean addOperationLog(OperaLogDO operaLogDO);

}
