package com.kht.backend.service.impl;

import com.kht.backend.dao.CapAcctDOMapper;
import com.kht.backend.dao.CustAcctDOMapper;
import com.kht.backend.dao.DepAcctDOMapper;
import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.AccountService;
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
    @Override
    public Result increaseCustomerAccount(CustAcctDO custAcctDO) {
        return null;
    }

    @Override
    public Result getCustomerAccount(int userCode) {
        return null;
    }

    @Override
    public Result increaseCapitalAccount(CapAcctDO capAcctDO) {
        return null;
    }

    @Override
    public Result getCapitalAccount(String customerCode) {
        return null;
    }

    @Override
    public Result increaseDeptAccount(DepAcctDO depAcctDO) {
        return null;
    }

    @Override
    public Result getDeptAccount(String custCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(custCode);
        List<DepAcctDO> depAcctDOList =capAcctDOList.stream()
                .map(i-> depAcctDOMapper.selectByCapCode(i.getCapCode()))
                .collect(Collectors.toList());
        return Result.OK(depAcctDOList).build();
    }
}
