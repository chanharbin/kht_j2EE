package com.kht.backend.service;

import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;

public interface UserService {
    //用户注册
    public Result userRegister(UserDO userDO);

    //查看账户信息
    public Result selectAccount(String customerId);

    //获取验证码
    public Result getOtp(String telphone);

    //修改密码
    public Result modifyPassword(String oldPassword,String newPassword,String capitalId);

    //修改用户信息
    public Result userModify(UserDO userDO);

    //用户登录
    public Result userLogin(String telphone,String password);

    //获取客户信息
    public Result selectUser(String customerId);

    //提交资料审核
    public Result validateUser(UserDO userDO);

    //增加资金账户
    public Result increaseCapitalAccount(CapAcctDO capAcctDO);


}
