package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.Result;

import java.util.List;
import java.util.Map;

public interface AccountService {
    public String increaseCustomerAccount(CustAcctDO custAcctDO);

    public Map<String,Object> getCustomerAccount(String customerCode);

    public String increaseCapitalAccount(String customerCode,String capitalAccountPassword) ;

    public List<Map<String,Object>>  getCapitalAccount(String customerCode);

    public String increaseDepositoryAccount(String capitalCode,String bankType,String bankCardCode);

    public List<Map<String,Object>> getDepositoryAccount(String customerCode);

    public String increaseTradeAccount(String customerCode,String stkEx,String stkBd, String custType,String trdUnit);
    public List<Map<String,Object>> getTradeAccount(String customerCode);

    public void modifyCapitalAccount(String caprCode,String oldPassword,String newPassword);
}
