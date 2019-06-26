package com.kht.backend.controller;

import com.kht.backend.entity.*;
import com.kht.backend.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    @RequestMapping(value = "/operation-log", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllOperationLogs(@RequestParam("pageNum") int pageNum) {
        return operationLogService.getAllOperationLogs(pageNum);
    }

    @RequestMapping(value = "/operation-log/{employeeCode}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getOperationLogsByEmployeeCode(@RequestParam("pageNum") int pageNum, @RequestParam("employeeCode") String employeeCode) {
        return operationLogService.getOperationLogsByEmployeeCode(pageNum, employeeCode);
    }

    @RequestMapping(value = "/operation-log/{logTime}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getOperationLogsByLogTime(@RequestParam("pageNum") int pageNum, @RequestParam("startTime") Long startTime, @RequestParam("endTime") Long endTime) {
        return operationLogService.getOperationLogsByLogTime(pageNum, startTime, endTime);
    }
}
