package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.entity.Result;

public interface AccountService {
    public Result increaseCustomerAccount();
    public CustAcctDO getCustomerAccount(int userCode);
    public Result increaseCapitalAccount();
    public CapAcctDO getCapitalAccount(String customerCode);
    public Result deleteCapitalAccount(String capitalAccount);
    public Result increaseDeptAccount();
}
