package com.kht.backend.controller;

import com.kht.backend.annotation.MethodLog;
import com.kht.backend.entity.*;
import com.kht.backend.service.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

@RestController
public class OperationLogController {

    @Autowired
    private OperationLogService operationLogService;

    /**
     * 获取所有操作记录
     * @param pageNum
     * @return
     */
    @MethodLog(17)
    @RequestMapping(value = "/operation-log", method = GET, produces = "application/json;charset=UTF-8")
    public Result getAllOperationLogs(@RequestParam("pageNum") int pageNum) {
        return operationLogService.getAllOperationLogs(pageNum);
    }

    /**
     * 根据操作人员获取操作记录
     * @param pageNum
     * @param employeeCode
     * @return
     */
    @MethodLog(18)
    @RequestMapping(value = "/operation-log/{employeeCode}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getOperationLogsByEmployeeCode(@RequestParam("pageNum") int pageNum, @PathVariable("employeeCode") String employeeCode) {
        return operationLogService.getOperationLogsByEmployeeCode(pageNum, employeeCode);
    }

    /**
     * 根据时间段获取操作记录
     * @param pageNum
     * @param startTime
     * @param endTime
     * @return
     */
    @MethodLog(19)
    @RequestMapping(value = "/operation-log/{startTime}/{endTime}", method = GET, produces = "application/json;charset=UTF-8")
    public Result getOperationLogsByLogTime(@RequestParam("pageNum") int pageNum, @PathVariable("startTime") Long startTime, @PathVariable("endTime") Long endTime) {
        return operationLogService.getOperationLogsByLogTime(pageNum, startTime, endTime);
    }
}
