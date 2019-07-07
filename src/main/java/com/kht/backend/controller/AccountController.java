package com.kht.backend.controller;

import com.kht.backend.entity.Result;
import com.kht.backend.service.impl.AccountServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class AccountController {
    @Autowired
    private AccountServiceImpl accountService;

    @GetMapping("/user/customer-account")
    public Result getUserCustomerAccount(@RequestParam("custCode") String customerCode) {
        Map<String, Object> custAcctDO = accountService.getCustomerAccountByCustCode(customerCode);
        return Result.OK(custAcctDO).build();
    }

    @GetMapping("/user/depository-account")
    public Result getUserDepositoryAccount(@RequestParam("custCode") String customerCode) {
        List<Map<String, Object>> depAcctDOList = accountService.getDepositoryAccountByCustCode(customerCode);
        return Result.OK(depAcctDOList).build();
    }

    @GetMapping("/user/trade-account")
    public Result getUserTradeAccount(@RequestParam("custCode") String customerCode) {
        List<Map<String, Object>> trdAcctDOList = accountService.getTradeAccount(customerCode);
        return Result.OK(trdAcctDOList).build();
    }

    @PostMapping("/user/trade-account")
    public Result increaseUserTradeAccount(@RequestParam("custCode") String customerCode,
                                           @RequestParam("stkEx") String stkEx,
                                           @RequestParam("stkBd") String stkBd,
                                           @RequestParam("trdUnit") String trdUnit) {
        accountService.increaseTradeAccount(customerCode, stkEx, stkBd, trdUnit);
        return Result.OK("添加证券账户成功").build();
    }

    @GetMapping("/user/capital-account")
    public Result getUserCapitalAccount(@RequestParam("custCode") String customerCode) {
        List<Map<String, Object>> capitalAccountInfoResponseList = accountService.getCapitalAccountByCustCode(customerCode);
        return Result.OK(capitalAccountInfoResponseList).build();
    }

    @PostMapping("/user/capital-account")
    public Result addUserCapitalAccount(@RequestParam("capPwd") String capPwd, @RequestParam("bankCardCode") String bankCardCode,
                                        @RequestParam("bankType") String bankType, @RequestParam("custCode") String custCode) {
        String capCode = accountService.increaseCapitalAccount(custCode, capPwd);
        accountService.increaseDepositoryAccount(capCode, bankType, bankCardCode);
        return Result.OK("增加资金账户和客户账户成功").build();
    }

    @PutMapping("/user/capital-account")
    public Result modifyUserCapitalAccountPassword(@RequestParam("capCode") String capCode, @RequestParam("oldPassword") String oldPassword,
                                                   @RequestParam("newPassword") String newPassword) {
        accountService.modifyCapitalAccount(capCode, oldPassword, newPassword);
        return Result.OK("修改资金密码成功").build();
    }

}
