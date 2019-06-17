package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.model.EmployeeModel;

public interface EmployeeService {

    public Result increaseEmployee(EmployeeModel employeeModel) throws ServiceException;

    public Result getEmployeeByName(String name);

    public Result getEmployee(int pageNum);

    public Result deleteEmployee(String id);

    public Result employeeLogin(String employeeId, String employeePwd);

    public Result modifyEmployee(EmployeeModel employeeModel);

    public Result getEmployeeById(String id);

}
