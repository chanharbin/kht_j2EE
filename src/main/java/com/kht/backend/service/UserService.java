/*
package com.kht.backend.service;

import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.DeptAcctDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;

public interface UserService {
    //用户注册
    public Result userRegister(Long telephone,String checkCode,String password);

    //查看账户信息
    public Result getUserAccountInfo(int userCode);

    //获取验证码
    public Result getOtp(String telephone);

    //修改密码
    public Result modifyCapitalAccountPassword(String oldPassword,String newPassword,String capitalCode);

    //用户登录
    public Result userLogin(Long telephone,String password);

    //获取客户信息
    public Result getUserInfo(String customerCode);

    //提交审核资料
    public Result increaseAccountOpenInfo(Long telephone,AcctOpenInfoDO acctOpenInfoDO);

    //增加资金账户
    public Result increaseCapitalAccount(String customerCode,String capitalAccountPassword,String bankType,String bankCardCode);

    //获取用户审核状态
    public Result getState(int infoCode);
}

*/
