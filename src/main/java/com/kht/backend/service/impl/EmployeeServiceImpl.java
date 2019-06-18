package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.EmployeeService;
import com.kht.backend.service.model.EmployeeModel;
import com.kht.backend.util.IdProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeDOMapper employeeDOMapper;
    @Autowired
    private IdProvider idProvider;
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;

    @Transactional
    @Override
    public Result deleteEmployee(String employeeCode) {
        if(employeeCode == null || employeeCode.equals("")){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工编号不存在");
        }
        EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(employeeCode);
        if(employeeDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工不存在");
        }
        int affectRow = employeeDOMapper.deleteByPrimaryKey(employeeCode);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"删除员工信息失败");
        }
        return Result.OK("删除员工成功").build();
    }

    @Transactional
    @Override
    public Result increaseEmployee(EmployeeDO employeeDO,String orgCode ) {
        String employeeId = idProvider.getId(orgCode);
        employeeDO.setEmployeeCode(employeeId);
        if(employeeDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"输入信息不完全");
        }
        int affectRow = employeeDOMapper.insertSelective(employeeDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"插入信息失败");
        }
        return Result.OK("添加员工信息成功").build();
    }

    @Override
    public Result employeeLogin(String employeeCode, String employeePwd) {
        return null;
    }

    @Transactional
    @Override
    public Result modifyEmployee(EmployeeDO employeeDO) {
        EmployeeDO employeeDOVer = employeeDOMapper.selectByPrimaryKey(employeeDO.getEmployeeCode());
        if(employeeDOVer == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工编号不存在");
        }
        int affectRow = employeeDOMapper.updateByPrimaryKey(employeeDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改员工信息失败");
        }
        else{
            return Result.OK("修改成功").build();
        }
    }

    @Override
    public Result getEmployeeByName(String name) {
        List<EmployeeDO> employeeDOList = employeeDOMapper.selectByName(name);
        if(employeeDOList == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"找不到员工信息");
        }
        else{
            return Result.OK(employeeDOList).build();
        }

    }

    @Override
    public Result listEmployee(int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<EmployeeDO> employeeDOList = employeeDOMapper.listAll();
        if(employeeDOList == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工信息获取失败");
        }
        PageInfo<EmployeeDO> page = new PageInfo<>(employeeDOList);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return Result.OK(resultData).build();
    }

    //TODO
    @Override
    public Result getUserValidationInfo(String customerCode) {
        AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByPrimaryKey(1);
        return null;
    }
    @Override
    public Result getEmployeeById(String id) {
        EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(id);
        if(employeeDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"获取员工信息失败");
        }
        return Result.OK(employeeDO).build();
    }
/*    @Transactional
    @Override
    public Result increaseEmployee(EmployeeModel employeeModel) throws ServiceException {
        if(employeeModel == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"输入信息不完全");
        }
        EmployeeDO employeeDO = new EmployeeDO();
        BeanUtils.copyProperties(employeeModel,employeeDO);
        int affectRow = employeeDOMapper.insertSelective(employeeDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"插入信息失败");
        }
        return Result.OK("添加员工信息成功").build();
    }


    @Override
    public Result getEmployeeByName(String name) {
        EmployeeDO employeeDO = employeeDOMapper.selectByName(name);
        EmployeeModel employeeModel = new EmployeeModel();
        if(employeeDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"获取员工信息失败");
        }
        BeanUtils.copyProperties(employeeDO,employeeModel);
        return Result.OK(employeeModel).build();
    }

    @Override
    public Result getEmployee(int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<EmployeeDO> employeeDOList = employeeDOMapper.listAll();
        List<EmployeeModel> employeeModelList =employeeDOList.stream().map(employeeDO->{
            EmployeeModel employeeModel = new EmployeeModel();
            BeanUtils.copyProperties(employeeDO,employeeModel);
            return employeeModel;
        }).collect(Collectors.toList());
        return Result.OK(employeeModelList).build();

    }

    @Transactional
    @Override
    public Result deleteEmployee(String id) {
        int affectRow = employeeDOMapper.deleteByPrimaryKey(id);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"删除员工信息失败");
        }
        return Result.OK("删除员工成功").build();
    }

    @Override
    public Result employeeLogin(String employeeId, String employeePwd) {
        EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(employeeId);
        if(employeeDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工用户名错误");
        }
        EmployeeModel employeeModel = new EmployeeModel();
        BeanUtils.copyProperties(employeeDO,employeeModel);
        if(!StringUtils.equals(employeePwd,employeeModel.getEmplyeePwd())){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工密码错误");
        }
        return Result.OK(employeeModel).build();
    }

    @Transactional
    @Override
    public Result modifyEmployee(EmployeeModel employeeModel) {
        EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(employeeModel.getEmployeeId());
        BeanUtils.copyProperties(employeeModel,employeeDO);
        if(employeeModel == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工不存在");
        }
        int affectRow = employeeDOMapper.updateByPrimaryKey(employeeDO);
        if(affectRow <= 0){
            throw  new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改信息失败");
        }
        return Result.OK("修改成功").build();
    }

    */
}
