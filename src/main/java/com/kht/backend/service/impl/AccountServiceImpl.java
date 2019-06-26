package com.kht.backend.service.impl;

import com.kht.backend.dao.CapAcctDOMapper;
import com.kht.backend.dao.CustAcctDOMapper;
import com.kht.backend.dao.DepAcctDOMapper;
import com.kht.backend.dao.TrdAcctDOMapper;
import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.AccountService;
import com.kht.backend.util.IdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;
    @Autowired
    private CapAcctDOMapper capAcctDOMapper;
    @Autowired
    private DepAcctDOMapper depAcctDOMapper;
    @Autowired
    private TrdAcctDOMapper trdAcctDOMapper;
    @Autowired
    private IdProvider idProvider;
    //新增客户账户
    @Override
    public Result increaseCustomerAccount(CustAcctDO custAcctDO) {
        custAcctDO.setCustCode(idProvider.getId(custAcctDO.getOrgCode()));
        int affectRow = custAcctDOMapper.insertSelective(custAcctDO);
        if(affectRow <= 0 ){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"开启客户账户失败");
        }
        return Result.OK("开启客户账户成功").build();
    }

    @Override
    public Result getCustomerAccount(String customerCode) {
        CustAcctDO custAcctDO=custAcctDOMapper.selectByPrimaryKey(customerCode);
        return Result.OK(custAcctDO).build();
    }

    //新增资金账户
    @Override
    public Result increaseCapitalAccount(CapAcctDO capAcctDO) {
        capAcctDO.setCapCode(idProvider.getId(capAcctDO.getOrgCode()));
        int affectRow = capAcctDOMapper.insertSelective(capAcctDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"增加资金账户失败");
        }
        return Result.OK("开启资金账户成功").build();
    }

    @Override
    public Result getCapitalAccount(String customerCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        return Result.OK(capAcctDOList).build();
    }

    //新增存管账户
    @Override
    public Result increaseDepositoryAccount(DepAcctDO depAcctDO) {
        depAcctDO.setDepCode(depAcctDO.getCapCode());
        int affectRow = depAcctDOMapper.insertSelective(depAcctDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"开启存管账户失败");
        }
        return Result.OK("开启存管账户成功").build();
    }

    @Override
    public Result getDepositoryAccount(String customerCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        List<DepAcctDO> depAcctDOList =capAcctDOList.stream()
                .map(i-> depAcctDOMapper.selectByCapCode(i.getCapCode()))
                .collect(Collectors.toList());
        return Result.OK(depAcctDOList).build();
    }

    @Override
    public Result increaseTradeAccount(TrdAcctDO trdAcctDO) {
        return null;
    }

    @Override
    public Result getTradeAccount(String customerCode) {
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByCustomerCode(customerCode);
        return Result.OK(trdAcctDOList).build();
    }

    @Override
    public Result modifyCapitalAccount(String capCode, String oldPassword, String newPassword) {
        CapAcctDO capAcctDO=capAcctDOMapper.selectByPrimaryKey(capCode);
        if(capAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        if(!capAcctDO.getCapPwd().equals(oldPassword)){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"密码错误");
        }
        capAcctDO.setCapPwd(newPassword);
        int affectRow=capAcctDOMapper.updateByPrimaryKeySelective(capAcctDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改密码失败");
        }
        return Result.OK("修改密码成功").build();
    }
}
