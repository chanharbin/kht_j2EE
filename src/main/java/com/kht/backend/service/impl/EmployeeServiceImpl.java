package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dao.OperaLogDOMapper;
import com.kht.backend.dao.UserDOMapper;
import com.kht.backend.dataobject.*;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.AccountService;
import com.kht.backend.service.EmployeeService;
import com.kht.backend.service.UserService;
import com.kht.backend.util.GetPoint;
import com.kht.backend.util.IdProvider;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
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
    @Autowired
    private OperaLogDOMapper operaLogDOMapper;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private AccountService accountService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private ValueOperations<String,Object> valueOperations;
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
        Integer userCode = employeeDO.getUserCode();
        int affectRow = employeeDOMapper.deleteByPrimaryKey(employeeCode);
        int affectRow1 = userDOMapper.deleteByPrimaryKey(userCode);
        if(affectRow <= 0 || affectRow1 <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"删除员工信息失败");
        }
        return Result.OK("删除员工成功").build();
    }

    @Transactional
    @Override
    public Result increaseEmployee(EmployeeDO employeeDO,String orgCode,UserDO userDO) {
        String employeeId = idProvider.getId(orgCode);
        employeeDO.setEmployeeCode(employeeId);
        int affectRow1 = userDOMapper.insertSelective(userDO);
        Integer userCode = userDO.getUserCode();
        employeeDO.setUserCode(userCode);
        if(employeeDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"输入信息不完全");
        }
        int affectRow = employeeDOMapper.insertSelective(employeeDO);
        if(affectRow <= 0 || affectRow1 <= 0){
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
    public Result modifyEmployee(EmployeeDO employeeDO,UserDO userDO) {
        EmployeeDO employeeDOVer = employeeDOMapper.selectByPrimaryKey(employeeDO.getEmployeeCode());
        if(employeeDOVer == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工编号不存在");
        }
        int affectRow = employeeDOMapper.updateByPrimaryKey(employeeDO);
        int affectRow1 = userDOMapper.updateByPrimaryKey(userDO);
        if(affectRow <= 0 || affectRow1 <= 0){
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
        List<EmployeeDO> employeeDOListFilterd = employeeDOList.stream().filter(emplist->!emplist.getEmployeeCode().isEmpty()).collect(Collectors.toList());
        if(employeeDOList == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"员工信息获取失败");
        }
        PageInfo<EmployeeDO> page = new PageInfo<>(employeeDOListFilterd);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("employee_num",page.getTotal());
        resultData.put("employees",page.getList());
        return Result.OK(resultData).build();
    }


    @Override
    public void getUserValidationInfo(int infoCode) {
        AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByInfoCode(infoCode);
        if(acctOpenInfoDO == null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资料编号错误");
        }
        CustAcctDO custAcctDO = new CustAcctDO();
        BeanUtils.copyProperties(acctOpenInfoDO,custAcctDO);
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(acctOpenInfoDO.getAnsOne());
        stringBuffer.append(acctOpenInfoDO.getAnsTwo());
        stringBuffer.append(acctOpenInfoDO.getAnsThree());
        stringBuffer.append(acctOpenInfoDO.getAnsFour());
        stringBuffer.append(acctOpenInfoDO.getAnsFive());
        stringBuffer.append(acctOpenInfoDO.getAnsSix());
        stringBuffer.append(acctOpenInfoDO.getAnsSeven());
        stringBuffer.append(acctOpenInfoDO.getAnsEight());
        stringBuffer.append(acctOpenInfoDO.getAnsNine());
        stringBuffer.append(acctOpenInfoDO.getAnsTen());
        char[] s = stringBuffer.toString().toCharArray();
        GetPoint getPoint = new GetPoint(s);
        String investorType = getPoint.getInvestorType();
        custAcctDO.setInvestorType(investorType);
        custAcctDO.setCloseTime(-1L);
        String customerCode = accountService.increaseCustomerAccount(custAcctDO);
        String capitalCode = accountService.increaseCapitalAccount(customerCode, "000000");
        accountService.increaseTradeAccount(customerCode,"0","00","0","0");
        accountService.increaseDepositoryAccount(capitalCode,"00",acctOpenInfoDO.getBankCardCode());
    }

    @Override
    public Result getEmployeeById(String id) {
        EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(id);
        if(employeeDO == null){
                throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"获取员工信息失败");
            }
        return Result.OK(employeeDO).build();
    }

}
