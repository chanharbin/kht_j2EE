package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.Result;

import java.util.List;
import java.util.Map;

public interface AccountService {
    //新增客户账户
    public String increaseCustomerAccount(CustAcctDO custAcctDO);
    //获取客户账户信息
    public Map<String,Object> getCustomerAccountByCustCode(String customerCode);
    //新增资金账户
    public String increaseCapitalAccount(String customerCode,String capitalAccountPassword) ;
    //获取资金账户
    public List<Map<String,Object>>  getCapitalAccountByCustCode(String customerCode);
    //修改资金账户信息
    public void modifyCapitalAccount(String caprCode,String oldPassword,String newPassword);
    //增加存管账户
    public String increaseDepositoryAccount(String capitalCode,String bankType,String bankCardCode);
    //获取存管账户
    public List<Map<String,Object>> getDepositoryAccountByCustCode(String customerCode);
    //新增证券账户
    public String increaseTradeAccount(String customerCode,String stkEx,String stkBd, String custType,String trdUnit);
    //获取证券账户
    public List<Map<String,Object>> getTradeAccount(String customerCode);


}
