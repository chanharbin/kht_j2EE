package com.kht.backend.service;

import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

public interface OperationLogService {

    public Result getOperationLogsByTime(int pageNum, Long startTime, Long endTime);

    public boolean addOperationLog(OperaLogDO operaLogDO);

}
