package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;

public interface UserService {
    //用户注册-1
    public Result userRegister(String telphone,String checkCode,String password);

    //查看账户信息-1
    public Result selectAccount(String customerId);

    //获取验证码-1
    public Result getOtp(String telphone);

    //修改密码-1
    public Result modifyPassword(String oldPassword,String newPassword,String capitalCode);

    //用户登录-1
    public Result userLogin(String telphone,String password);

    //获取客户信息-1
    public Result selectUser(String customerCode);

    //提交资料审核-1
    public Result validateUser(UserDO userDO);

    //增加资金账户-1
    public Result increaseCapitalAccount(String capitalPwd,String bankCardCode,String bankType,String customerCode);

    //获取用户审核状态-1
    public Result getState(int infoCode);



}
