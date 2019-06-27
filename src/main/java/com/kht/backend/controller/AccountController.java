package com.kht.backend.controller;

import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.impl.AccountServiceImpl;
import com.kht.backend.service.model.CapitalAccountInfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public class AccountController {

    @Autowired
    private AccountServiceImpl accountService;
    @GetMapping("/user/customer-account")
    public Result getUserCustomerAccount(@RequestParam("custCode")String customerCode){
        CustAcctDO custAcctDO=accountService.getCustomerAccount(customerCode);
        return Result.OK(custAcctDO).build();
    }
    @GetMapping("/user/depository-account")
    public Result getUserDepositoryAccount(@RequestParam("custCode")String customerCode){
        List<DepAcctDO> depAcctDOList= accountService.getDepositoryAccount(customerCode);
        return Result.OK(depAcctDOList).build();
    }
    @GetMapping("/user/trade-account")
    public Result getUserTradeAccount(@RequestParam("custCode")String customerCode){
        List<TrdAcctDO>  trdAcctDOList=accountService.getTradeAccount(customerCode);
        return Result.OK(trdAcctDOList).build();
    }

    @GetMapping("/user/capital-account")
    public Result getUserCapitalAccount(@RequestParam("custCode")String customerCode){
        List<CapitalAccountInfoResponse> capitalAccountInfoResponseList =accountService.getCapitalAccountInfo(customerCode);
        return Result.OK(capitalAccountInfoResponseList).build();
    }
    @PostMapping("/user/capital-account")
    public Result addUserCapitalAccount(@RequestParam("capPwd")String capPwd,@RequestParam("bankCardCode")String bankCardCode,
                                        @RequestParam("bankType")String bankType,@RequestParam("custCode")String custCode){
        String capCode=accountService.increaseCapitalAccount(custCode,capPwd);
        accountService.increaseDepositoryAccount(capCode,bankType,bankCardCode);
        return Result.OK("增加资金账户和客户账户成功").build();
    }
    @PutMapping("/user/capital-account")
    public Result modifyUserCapitalAccountPassword(@RequestParam("capCode")String capCode,@RequestParam("oldPassword")String oldPassword,
                                                   @RequestParam("newPassword")String newPassword){
        accountService.modifyCapitalAccount(capCode,oldPassword,newPassword);
        return  Result.OK("修改资金密码成功").build();
    }

}
