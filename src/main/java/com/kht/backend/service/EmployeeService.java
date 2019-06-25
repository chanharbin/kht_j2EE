package com.kht.backend.service;

import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.EmployeeModel;

public interface EmployeeService {
    //删除员工
    public Result deleteEmployee(String employeeCode);

    //新增员工账号
    public Result increaseEmployee(EmployeeDO employeeDO, String orgCode,UserDO userDO);

    //员工登录
    public Result employeeLogin(String employeeCode,String employeePwd);

    //修改员工信息
    public Result modifyEmployee(EmployeeDO employeeDO, UserDO userDO);

    //根据姓名获取员工列表
    public Result getEmployeeByName(String name);

    //获取员工列表
    public Result listEmployee(int pageNum);

    //获取用户审核资料
    public Result getUserValidationInfo(String customerCode);

    //根据员工编号获取员工
    public Result getEmployeeById(String id);

    //增加操作记录
    public Result increaseOperationLog(OperaLogDO operaLogDO);

    //获取所有的操作记录列表
    public Result getOperationLogList(int pageNum);

    //获取指定人员的操作记录
    public Result getOperationLogByCode(String operator,int pageNum);

    //获取某个时间段之间的操作记录
    public Result getOperationLogBetweenTime(long startTime,long endTime,int pageNum);

}
