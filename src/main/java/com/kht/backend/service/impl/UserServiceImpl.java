package com.kht.backend.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.*;
import com.kht.backend.dataobject.*;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.UserService;
import com.kht.backend.service.model.DictionaryModel;
import com.kht.backend.service.model.CapitalAccountInfoResponse;
import com.kht.backend.service.model.UserListResponse;
import com.kht.backend.util.IdProvider;
import com.kht.backend.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
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
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private MainDataDictDOMapper mainDataDictDOMapper;
    @Autowired
    private SubDataDictDOMapper subDataDictDOMapper;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private ValueOperations<String, Object> valueOperations;
    @Value("${app.pageSize}")
    private int pageSize;
    private final String otpKey="otp";
    //验证码过期时间 单位秒
    private  long otpExpirationInSecond=900;
    @Override
    public Result userRegister(Long telephone, int checkCode, String password) {
        int realOtp;
        if (redisTemplate.hasKey(otpKey+telephone.toString())) {
            realOtp=(int)valueOperations.get(otpKey+telephone.toString());
        }
        else{
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "无验证码或验证码过期");
        }
        if(realOtp!=checkCode){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "验证码错误");
        }
        UserDO userDO = new UserDO();
        userDO.setTelephone(telephone);
        userDO.setPassword(password);
        userDO.setUserType("0");
        int affectRow = userDOMapper.insertSelective(userDO);
        if (affectRow == 0) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "注册失败");
        }
        return Result.OK("注册成功").build();
    }

    @Override
    @Transactional
    public void increaseAccountOpenInfo(int userCode, AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO) {
        AcctOpenInfoDO acctOpenInfoDO1 = acctOpenInfoDOMapper.selectByUserCode(userCode);
        int affectRow1 = imageDOMapper.insertSelective(imageDO);
        if (affectRow1 == 0) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "添加影像资料失败");
        }
        int affectRow;
        UserDO userDO = userDOMapper.selectByPrimaryKey(userCode);
        acctOpenInfoDO.setUserCode(userDO.getUserCode());
        acctOpenInfoDO.setImgCode(imageDO.getImgCode());
        //0表示提交未审核
        acctOpenInfoDO.setInfoStatus("0");
        acctOpenInfoDO.setCmtTime(new Date().getTime());
        if (acctOpenInfoDO1 != null) {
            if(acctOpenInfoDO1.getInfoCode()==1) {
                throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "已开户不能重复提交");
            }else{
                affectRow=acctOpenInfoDOMapper.updateByPrimaryKeySelective(acctOpenInfoDO);
            }
        }else {
            affectRow = acctOpenInfoDOMapper.insertSelective(acctOpenInfoDO);
        }
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "添加开户资料失败");
        }

    }

    public Map<String,Object> getAccountOpeningInfo(int userCode) {
        String tabCode="acct_open_info";
        AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByUserCode(userCode);
        if (acctOpenInfoDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "未提交开户资料");
        }
        ImageDO imageDO = imageDOMapper.selectByPrimaryKey(acctOpenInfoDO.getImgCode());
        if (imageDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "未提交影像资料");
        }

        String orgName = redisService.getOrganizationName(acctOpenInfoDO.getOrgCode());
        String gender = redisService.getDataDictionary("GENDER" , tabCode , acctOpenInfoDO.getGender());
        String idType = redisService.getDataDictionary("ID_TYPE" , tabCode, acctOpenInfoDO.getIdType());
        String education=redisService.getDataDictionary("EDUCATION" , tabCode, acctOpenInfoDO.getEducation());
        //String bankType=redisService.getDataDictionary("BANK_TYPE",tabCode,acctOpenInfoDO.getBankType());
        //String openChannel=redisService.getDataDictionary("OPEN_CHANNEL",tabCode,acctOpenInfoDO.getOpenChannel());
        //String infoStatus=redisService.getDataDictionary("INFO_STATUS",tabCode,acctOpenInfoDO.getInfoStatus());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("infoCode", acctOpenInfoDO.getInfoCode());
        data.put("imgCode", acctOpenInfoDO.getImgCode());
        data.put("name", acctOpenInfoDO.getName());
        data.put("gender", gender);
        data.put("idType", idType);
        data.put("idCode", acctOpenInfoDO.getIdCode());
        data.put("idEffDate", acctOpenInfoDO.getIdEffDate());
        data.put("idExpDat", acctOpenInfoDO.getIdExpDate());
        data.put("telephone", acctOpenInfoDO.getTelephone());
        data.put("email", acctOpenInfoDO.getEmail());
        data.put("address", acctOpenInfoDO.getAddress());
        data.put("occupation", acctOpenInfoDO.getOccupation());
        data.put("company", acctOpenInfoDO.getCompany());
        data.put("education", education);
        data.put("orgName", orgName);
        data.put("idFront", imageDO.getIdFront());
        data.put("idBack", imageDO.getIdBack());
        data.put("face", imageDO.getFace());
        return data;
    }

    @Override
    public void getOtp(Long telephone) {
        int otpValue=(int)(Math.random()*10000);
        valueOperations.set(otpKey+telephone,otpValue,otpExpirationInSecond, TimeUnit.SECONDS);
        logger.info("telephone "+telephone+" get checkCode :"+otpValue);
    }

    @Override
    public Result getUserAccountInfo(int userCode) {
        CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userCode);
        if (custAcctDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户未开户");
        }
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByCustomerCode(custAcctDO.getCustCode());
        List<DepAcctDO> depAcctDOList = capAcctDOList.stream()
                .map(i -> depAcctDOMapper.selectByCapCode(i.getCapCode()))
                .collect(Collectors.toList());
        List<TrdAcctDO> trdAcctDOList = trdAcctDOMapper.selectByCustomerCode(custAcctDO.getCustCode());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("capital_accounts", capAcctDOList);
        data.put("securities_accounts", trdAcctDOList);
        data.put("depository_accounts", depAcctDOList);
        return new Result(200, "OK", data);
    }

    @Override
    public void modifyUserPassword(int userCode, String oldPassword, String password) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(userCode);
        if (userDO == null || !userDO.getPassword().equals(passwordEncoder.encode(oldPassword))) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "密码错误");
        }
        userDO.setPassword(passwordEncoder.encode(password));
        int affectRow = userDOMapper.updateByPrimaryKeySelective(userDO);
        if (affectRow == 0) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "更新用户信息失败");
        }

    }

    @Override
    public Result getUserInfo(int userCode) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(userCode);
        if (userDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户不存在");
        }
        /* Map<String, Object> resultData = new LinkedHashMap<>();
        resultData.put("data",userDO);*/
        return Result.OK(userDO).build();
    }



    @Override
    public Map<String, Object> getUserAndState(int userCode) {
        AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByUserCode(userCode);
        CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userCode);
        if (acctOpenInfoDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "获取用户开户信息失败");
        }
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("infoStatus", redisService.getDataDictionary("INFO_STATUS","acct_open_info",acctOpenInfoDO.getInfoStatus()));
        if (custAcctDO == null) {
            data.put("custCode", null);
        } else {
            data.put("custCode", custAcctDO.getCustCode());
        }
        return data;
    }

    public Map<String, Object> getUserInfoList(int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<AcctOpenInfoDO> acctOpenInfoDOList = acctOpenInfoDOMapper.listAll();
        List<AcctOpenInfoDO> acctOpenInfoDOListFiltered = acctOpenInfoDOList.stream().filter(acctOpenInfoDO -> acctOpenInfoDO.getInfoStatus().equals("0")).collect(Collectors.toList());
        if (acctOpenInfoDOList == null || acctOpenInfoDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户列表不存在");
        }
        PageInfo<AcctOpenInfoDO> page = new PageInfo<>(acctOpenInfoDOListFiltered);
        List<UserListResponse> userListResponseList = page.getList().stream()
                .map(i -> new UserListResponse(i.getUserCode(),i.getInfoCode(), i.getName(), i.getIdType(), i.getIdCode(),
                        organizationDOMapper.selectByPrimaryKey(i.getOrgCode()).getOrgName(), i.getEmail()))
                .collect(Collectors.toList());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("userList", userListResponseList);
        return data;
    }

    @Override
    public Map<String, Object> getList(int pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        List<AcctOpenInfoDO> acctOpenInfoDOList = acctOpenInfoDOMapper.listAll();
        if (acctOpenInfoDOList == null || acctOpenInfoDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户列表不存在");
        }
        PageInfo<AcctOpenInfoDO> page = new PageInfo<>(acctOpenInfoDOList);
        List<UserListResponse> userListResponseList = page.getList().stream()
                .map(i -> new UserListResponse(i.getUserCode(),i.getInfoCode(), i.getName(), i.getIdType(), i.getIdCode(),
                        organizationDOMapper.selectByPrimaryKey(i.getOrgCode()).getOrgName(), i.getEmail()))
                .collect(Collectors.toList());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("userList", userListResponseList);
        return data;
    }

    public List<DictionaryModel> getAllDataInfoList(String colCode, String tabCode) {
        MainDataDictDO mainDataDictDO = mainDataDictDOMapper.selectByColCodeAndTabCode(colCode, tabCode);
        if (mainDataDictDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "数据字典中不存在该类型");
        }
        List<SubDataDictDO> subDataDictDOList = subDataDictDOMapper.selectByMainCode(mainDataDictDO.getMainCode());
        if (subDataDictDOList == null || subDataDictDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "数据字典中不存在信息");
        }
        List<DictionaryModel> dictionaryModelList = subDataDictDOList.stream()
                .map(i -> new DictionaryModel(i.getValue(), i.getValueCode()))
                .collect(Collectors.toList());
        return dictionaryModelList;
    }


    //废弃不确定是否要用
    //public List<>
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
         /*
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
    }*/
    /*@Override
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
    }*/
}

