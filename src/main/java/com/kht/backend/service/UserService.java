package com.kht.backend.service;

import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.ImageDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.model.DictionaryModel;

import java.util.List;
import java.util.Map;

public interface UserService {
    //获取验证码
    public void getOtp(Long telephone);

    //用户注册
    public void userRegister(Long telephone, int checkCode, String password);

    //获取用户信息
    public UserDO getUserInfo(int userCode);

    //更改用户密码
    public void modifyUserPassword(int userCode, String oldPassword, String password);

    //获取用户列表
    public Map<String, Object> getUserInfoList(int pageNum, boolean filterable,String employeeCode);

    //获取用户审核状态
    public Map<String, Object> getUserAndState(int userCode);

    //增加或修改用户开户资料
    public void increaseAccountOpenInfo(int userCode, AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO);

    //获取用户开户资料
    public Map<String, Object> getAccountOpeningInfo(int userCode);

    //获取数据字典里面的数据列表
    public List<DictionaryModel> getAllDataInfoList(String colCode, String tabCode);
}
