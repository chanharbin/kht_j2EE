package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.Result;

public interface AccountService {
    public Result increaseCustomerAccount(CustAcctDO custAcctDO);
    public Result getCustomerAccount(String customerCode);
    public Result increaseCapitalAccount(CapAcctDO capAcctDO);
    public Result getCapitalAccount(String customerCode);
    //public Result deleteCapitalAccount(String capitalAccount);
    public Result increaseDepositoryAccount(DepAcctDO depAcctDO);
    public Result getDepositoryAccount(String customerCode);
    public Result increaseTradeAccount(TrdAcctDO trdAcctDO);
    public Result getTradeAccount(String customerCode);

    public Result modifyCapitalAccount(String caprCode,String oldPassword,String newPassword);
}
