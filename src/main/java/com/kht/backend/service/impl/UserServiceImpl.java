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
import org.springframework.transaction.annotation.Transactional;

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
    private DepAcctDOMapper depAcctDOMapper;
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;
    @Autowired
    private ImageDOMapper imageDOMapper;
    @Override
    public Result userRegister(Long telephone, String checkCode, String password) {
        if(checkCode==null);//TODO
        UserDO userDO=new UserDO();
        userDO.setTelephone(telephone);
        userDO.setPassword(password);
        userDO.setUserType("0");
        int affectRow=userDOMapper.insertSelective(userDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"注册失败");
        }
        return Result.OK("注册成功").build();
    }
    @Override
    @Transactional
    public Result increaseAccountOpenInfo(int userCode,AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO){
        int affectRow1=imageDOMapper.insertSelective(imageDO);
        if(affectRow1==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"添加影像资料失败");
        }
        UserDO userDO=userDOMapper.selectByPrimaryKey(userCode);
        acctOpenInfoDO.setUserCode(userDO.getUserCode());
        acctOpenInfoDO.setImgCode(imageDO.getImgCode());
        //0表示提交未审核
        acctOpenInfoDO.setInfoStatus("0");
        acctOpenInfoDO.setCmtTime(new Date().getTime());
        int affectRow=acctOpenInfoDOMapper.insertSelective(acctOpenInfoDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"添加开户资料失败");
        }
        return Result.OK("添加开户资料成功").build();
    }
    public Result getAccountOpeningInfo(int userCode){
        AcctOpenInfoDO acctOpenInfoDO=acctOpenInfoDOMapper.selectByUserCode(userCode);
        if(acctOpenInfoDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"未提交开户资料");
        }
        ImageDO imageDO=imageDOMapper.selectByPrimaryKey(acctOpenInfoDO.getImgCode());
        if(imageDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"未提交影像资料");
        }
        String orgName=null;//TODO从redis读
        Map<String,Object>data=new LinkedHashMap<>();
        data.put("infoCode",acctOpenInfoDO.getInfoCode());
        data.put("imgCode",acctOpenInfoDO.getImgCode());
        data.put("name",acctOpenInfoDO.getName());
        data.put("gender",acctOpenInfoDO.getGender());
        data.put("idType",acctOpenInfoDO.getIdType());
        data.put("idCode",acctOpenInfoDO.getIdCode());
        data.put("idEffDate",acctOpenInfoDO.getIdEffDate());
        data.put("idExpDat",acctOpenInfoDO.getIdExpDate());
        data.put("telephone",acctOpenInfoDO.getTelephone());
        data.put("email",acctOpenInfoDO.getEmail());
        data.put("address",acctOpenInfoDO.getAddress());
        data.put("occupation",acctOpenInfoDO.getOccupation());
        data.put("company",acctOpenInfoDO.getCompany());
        data.put("education",acctOpenInfoDO.getEducation());
        data.put("orgName",orgName);
        data.put("idFront",imageDO.getIdFront());
        data.put("idBack",imageDO.getIdBack());
        data.put("face",imageDO.getFace());
        return new Result(200,"ok",data);
    }

    @Override
    public Result getOtp(String telephone) {
        //TODO redis
        return null;
    }
    @Override
    public Result getUserAccountInfo(int userCode) {
        CustAcctDO custAcctDO=custAcctDOMapper.selectByUserCode(userCode);
        if(custAcctDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"用户未开户");
        }
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(custAcctDO.getCustCode());
        List<DepAcctDO> depAcctDOList =capAcctDOList.stream()
                .map(i-> depAcctDOMapper.selectByCapCode(i.getCapCode()))
                .collect(Collectors.toList());
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByCustomerCode(custAcctDO.getCustCode());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("capital_accounts",capAcctDOList);
        data.put("securities_accounts",trdAcctDOList);
        data.put("depository_accounts", depAcctDOList);
        return new Result(200,"OK",data);
    }
    @Override
    public Result modifyUserPassword(int userCode,String oldPassword,String password) {
        UserDO userDO=userDOMapper.selectByPrimaryKey(userCode);
        if(userDO==null||!userDO.getPassword().equals(oldPassword)){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"更新用户信息失败");
        }
        userDO.setPassword(password);
        int affectRow=userDOMapper.updateByPrimaryKeySelective(userDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"更新用户信息失败");
        }
        return Result.OK("更新用户信息成功").build();
    }
    @Override
    public Result getUserInfo(int userCode) {
        UserDO userDO=userDOMapper.selectByPrimaryKey(userCode);
        if(userDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"用户不存在");
        }
        /* Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("data",userDO);*/
        return Result.OK(userDO).build();
    }
    @Override
    public Result increaseCapitalAccount(String customerCode,String capitalAccountPassword,String bankType,String bankCardCode){
        CustAcctDO custAcctDO=custAcctDOMapper.selectByPrimaryKey(customerCode);
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        CapAcctDO capAcctDO=new CapAcctDO();
        capAcctDO.setCapCode(idProvider.getId(custAcctDO.getOrgCode()));
        capAcctDO.setCustCode(customerCode);
        capAcctDO.setOrgCode(custAcctDO.getOrgCode());
        capAcctDO.setCapPwd(capitalAccountPassword);
        capAcctDO.setCurrency("0");
        capAcctDO.setMainFlag(capAcctDOList.size()==0);
        capAcctDO.setAttr("0");
        capAcctDO.setOpenTime(new Date().getTime());
        capAcctDO.setCloseTime(0L);
        capAcctDO.setCapStatus("0");

        DepAcctDO depAcctDO =new DepAcctDO();
        depAcctDO.setDepCode(capAcctDO.getCapCode());
        depAcctDO.setCapCode(capAcctDO.getCapCode());
        depAcctDO.setBankType(bankType);
        depAcctDO.setBankCardCode(bankCardCode);
        depAcctDO.setOpenTime(new Date().getTime());
        depAcctDO.setCloseTime(0L);
        depAcctDO.setDepStatus("0");

        int capAffectRow=capAcctDOMapper.insertSelective(capAcctDO);
        int deptAffectRow= depAcctDOMapper.insertSelective(depAcctDO);
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
    public Result getState(int userCode) {
        AcctOpenInfoDO acctOpenInfoDO=acctOpenInfoDOMapper.selectByUserCode(userCode);
        if(acctOpenInfoDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"获取用户开户信息失败");
        }
        return Result.OK(acctOpenInfoDO.getInfoStatus()).build();
    }
     /*@Override
    public Result userLogin(Long telephone, String password) {
        UserDO userDO=userDOMapper.selectByTelephone(telephone);
        if(userDO==null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"用户名不存在");
        }
        if(!userDO.getPassword().equals(password)){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"密码错误");
        }
        return Result.OK(userDO.getUserCode()).build();
    }*/
}

