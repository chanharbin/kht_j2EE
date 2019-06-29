package com.kht.backend.service;

import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.ImageDO;
import com.kht.backend.entity.Result;

import java.util.Map;

public interface UserService {
    //用户注册
    public Result userRegister(Long telephone,int checkCode,String password);

    //查看账户信息
    public Result getUserAccountInfo(int userCode);

    //获取验证码
    public void getOtp(Long telephone);

    //修改密码
    //public Result modifyCapitalAccountPassword(String oldPassword,String newPassword,String capitalCode);


    //获取未审核客户信息
    public Result getUserInfo(int userCode);

    //获取全部客户信息
    public Map<String,Object> getList(int pageNum);

    //提交审核资料
    public void increaseAccountOpenInfo(int userCode, AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO);

    //增加资金账户
    //public Result increaseCapitalAccount(String customerCode,String capitalAccountPassword,String bankType,String bankCardCode);

    //获取用户审核状态
    public Map<String,Object> getUserAndState(int userCode);

    //修改用户密码
    public void modifyUserPassword(int userCode,String oldPassword,String password);
}
