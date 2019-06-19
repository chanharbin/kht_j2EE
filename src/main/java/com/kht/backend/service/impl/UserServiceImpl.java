package com.kht.backend.service.impl;


import com.kht.backend.dao.*;
import com.kht.backend.dataobject.*;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.UserService;
import com.kht.backend.util.IdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private IdProvider idProvider;
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private CapAcctDOMapper capAcctDOMapper;
    @Autowired
    private TrdAcctDOMapper trdAcctDOMapper;
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;
    @Autowired
    private DeptAcctDOMapper deptAcctDOMapper;
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;

    @Override
    public Result userRegister(Long telephone, String checkCode, String password) {
        if(checkCode==null);//TODO
        UserDO userDO=new UserDO();
        userDO.setTelephone(telephone);
        userDO.setPassword(password);
        int affectRow=userDOMapper.insertSelective(userDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"注册失败");
        }
        return Result.OK("注册成功").build();
    }
    @Override
    public Result getOtp(String telephone) {
        //TODO redis
        return null;
    }
    @Override
    public Result getUserAccountInfo(int userCode) {
        CustAcctDO custAcctDO=custAcctDOMapper.selectByUserCode(userCode);
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        List<DeptAcctDO> deptAcctDOList=capAcctDOList.stream()
                .map(i->deptAcctDOMapper.selectByPrimaryKey(i.getDepCode()))
                .collect(Collectors.toList());
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByCustomerCode(customerCode);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("capital_accounts",capAcctDOList);
        data.put("securities_accounts",trdAcctDOList);
        data.put("depository_accounts",deptAcctDOList);
        return new Result(200,"OK",data);
    }
    @Override
    public Result userLogin(Long telephone, String password) {
        UserDO userDO=userDOMapper.selectByTelephone(telephone);
        if(userDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"用户名不存在");
        }
        if(!userDO.getPassword().equals(password)){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"密码错误");
        }
        return Result.OK(userDO.getCustCode()).build();
    }
    @Override
    public Result getUserInfo(String customerCode) {
        UserDO userDO=userDOMapper.selectByCustomerCode(customerCode);
        if(userDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"用户不存在");
        }
    */
/*    Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("data",userDO);*//*

        return Result.OK(userDO).build();
    }
    @Override
    public Result increaseAccountOpenInfo(Long telephone,AcctOpenInfoDO acctOpenInfoDO){
        //TODO 图片数据库
        int affectRow=acctOpenInfoDOMapper.insertSelective(acctOpenInfoDO);
        int affectRow1=userDOMapper.updateInfoCodeByTelephone(telephone,acctOpenInfoDO.getInfoCode());
        if(affectRow==0||affectRow1==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"添加开户资料失败");
        }
        return Result.OK("添加开户资料成功").build();
    }
    @Override
    public Result increaseCapitalAccount(String customerCode,String capitalAccountPassword,String bankType,String bankCardCode){
        CustAcctDO custAcctDO=custAcctDOMapper.selectByPrimaryKey(customerCode);
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        CapAcctDO capAcctDO=new CapAcctDO();
        capAcctDO.setCapCode(idProvider.getId(custAcctDO.getOrgCode()));
        capAcctDO.setCustCode(customerCode);
        capAcctDO.setDepCode(capAcctDO.getCapCode());
        capAcctDO.setCapPwd(capitalAccountPassword);
        capAcctDO.setCurrency("0");
        capAcctDO.setMainFlag(capAcctDOList.size()==0);
        capAcctDO.setAttr("0");
        capAcctDO.setOpenTime(new Date().getTime());
        capAcctDO.setCloseTime(0L);
        capAcctDO.setCapStatus("0");

        DeptAcctDO deptAcctDO=new DeptAcctDO();
        deptAcctDO.setDepCode(capAcctDO.getDepCode());
        deptAcctDO.setBankType(bankType);
        deptAcctDO.setBankCardCode(bankCardCode);
        deptAcctDO.setOpenTime(new Date().getTime());
        deptAcctDO.setCloseTime(0L);
        deptAcctDO.setDepStatus("0");

        int deptAffectRow=deptAcctDOMapper.insertSelective(deptAcctDO);
        int capAffectRow=capAcctDOMapper.insertSelective(capAcctDO);
        if(capAffectRow==0||deptAffectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"增加资金账户失败");
        }
        return Result.OK("更新用户信息成功").build();
    }
    @Override
    public Result modifyCapitalAccountPassword(String oldPassword, String newPassword, String capitalCode) {
        CapAcctDO capAcctDO=capAcctDOMapper.selectByPrimaryKey(capitalCode);
        if(!capAcctDO.getCapPwd().equals(oldPassword)){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"密码错误");
        }
        if(newPassword==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"密码为空");
        }
        int affectRow=capAcctDOMapper.updatePasswordByPrimaryKey(capitalCode,newPassword);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"资金账户不存在");
        }
        return Result.OK("更新资金账户密码成功").build();
    }
    @Override
    public Result getState(int infoCode) {
        AcctOpenInfoDO acctOpenInfoDO=acctOpenInfoDOMapper.selectByPrimaryKey(infoCode);
        if(acctOpenInfoDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"获取用户开户信息失败");
        }
        return Result.OK(acctOpenInfoDO.getInfoStatus()).build();
    }

    public Result modifyUserInfo(UserDO userDO) {
        int affectRow=userDOMapper.updateByPrimaryKeySelective(userDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"更新用户信息失败");
        }
        return Result.OK("更新用户信息成功").build();
    }
}

