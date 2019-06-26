package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

public interface OperationLogService {
    //

    public Result getOperationLogsByTime(Long startTime, Long endTime);

    public boolean addOperationLog();

}
