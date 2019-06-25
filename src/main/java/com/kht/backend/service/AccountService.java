package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.entity.Result;

public interface AccountService {
    public Result increaseCustomerAccount(CustAcctDO custAcctDO);
    public Result getCustomerAccount(int userCode);
    public Result increaseCapitalAccount(CapAcctDO capAcctDO);
    public Result getCapitalAccount(String customerCode);
    //public Result deleteCapitalAccount(String capitalAccount);
    public Result increaseDeptAccount(DepAcctDO depAcctDO);
    public Result getDeptAccount(String custCode);
}
