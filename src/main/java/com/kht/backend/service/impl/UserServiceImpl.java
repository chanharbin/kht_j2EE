package com.kht.backend.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
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
import com.kht.backend.util.GetPoint;
import com.kht.backend.util.IdProvider;
import com.kht.backend.util.JwtTokenProvider;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
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
    private CustAcctDOMapper custAcctDOMapper;
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

    private ObjectMapper objectMapper = new ObjectMapper();
    private final String otpKey = "otp";
    //验证码过期时间 单位秒
    private long otpExpirationInSecond = 900;

    /**
     * 获取验证码
     *
     * @param telephone
     */
    @Override
    public void getOtp(Long telephone) {
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int otpValue = random.nextInt(8999);
        otpValue += 1000;
        //int otpValue = (int) (Math.random() * 10000);
        valueOperations.set(otpKey + telephone, otpValue, otpExpirationInSecond, TimeUnit.SECONDS);
        logger.info("telephone " + telephone + " get checkCode :" + otpValue);
    }

    /**
     * 用户注册
     *
     * @param telephone
     * @param checkCode
     * @param password
     */
    @Override
    @Transactional
    public void userRegister(Long telephone, int checkCode, String password) {
        int realOtp;
        if (redisTemplate.hasKey(otpKey + telephone.toString())) {
            realOtp = (int) valueOperations.get(otpKey + telephone.toString());
        } else {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "无验证码或验证码过期");
        }
        if (realOtp != checkCode) {
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

    }

    /**
     * 获取用户信息
     *
     * @param userCode
     * @return
     */
    @Override
    public UserDO getUserInfo(int userCode) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(userCode);
        if (userDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户不存在");
        }
        return userDO;
    }

    /**
     * 修改用户密码
     *
     * @param userCode
     * @param oldPassword
     * @param password
     */
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

    /**
     * 获取用户列表
     *
     * @param pageNum
     * @param filterable true 代表只选出未审核的用户； false 代表选出所有用户
     * @return
     */
    @Override
    public Map<String, Object> getUserInfoList(int pageNum, boolean filterable, String employeeCode) {
        String orgCode = employeeCode.substring(0, 4);
        System.out.println(orgCode);
        Page<Object> objectPage = PageHelper.startPage(pageNum, Integer.parseInt(redisService.getParaValue("pageSize")));
        List<AcctOpenInfoDO> acctOpenInfoDOList;
        String tabCode = "acct_open_info";
        if (orgCode.equals("0000")) {
            if (filterable) {
                //acctOpenInfoDOList = acctOpenInfoDOList.stream().filter(acctOpenInfoDO -> acctOpenInfoDO.getInfoStatus().equals("0")).collect(Collectors.toList());
                acctOpenInfoDOList = acctOpenInfoDOMapper.listUnauditedUser();
            } else {
                acctOpenInfoDOList = acctOpenInfoDOMapper.listAll();
            }
        } else {
            if (filterable) {
                //acctOpenInfoDOList = acctOpenInfoDOList.stream().filter(acctOpenInfoDO -> acctOpenInfoDO.getInfoStatus().equals("0")).collect(Collectors.toList());
                acctOpenInfoDOList = acctOpenInfoDOMapper.listUnauditedUserByOrg(orgCode);
            } else {
                acctOpenInfoDOList = acctOpenInfoDOMapper.listAllByOrg(orgCode);
            }
        }
        if (acctOpenInfoDOList == null || acctOpenInfoDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户列表不存在");
        }
        PageInfo<AcctOpenInfoDO> page = new PageInfo<>(acctOpenInfoDOList);
        if (page.getList() == null || page.getList().isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户列表不存在");
        }
        List<UserListResponse> userListResponseList = page.getList().stream()
                .map(i -> new UserListResponse(i.getUserCode(), i.getInfoCode(), i.getName(),
                        redisService.getDataDictionary("ID_TYPE", tabCode, i.getIdType()), i.getIdCode(),
                        redisService.getOrganizationName(i.getOrgCode()), i.getEmail()))
                .collect(Collectors.toList());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("userList", userListResponseList);
        data.put("pageSize", Integer.parseInt(redisService.getParaValue("pageSize")));
        return data;
    }

    public Map<String, Object> getUserListByEmployeeCodeAndStartTimeAndEndTIme(int pageNum, String employeeCode, Long startTime, Long endTime) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, Integer.parseInt(redisService.getParaValue("pageSize")));
        String tabCode = "acct_open_info";
        if (startTime == null || endTime == null) {
            startTime = getNeedTime(0, 0, 0, 0).getTime();
            endTime = getNeedTime(23, 59, 59, 0).getTime();
        }
        List<AcctOpenInfoDO> acctOpenInfoDOList = acctOpenInfoDOMapper.selectByEmployeeCodeAndStartTimeAndEndTime(employeeCode, startTime, endTime);
        if (acctOpenInfoDOList == null || acctOpenInfoDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户列表不存在");
        }
        PageInfo<AcctOpenInfoDO> page = new PageInfo<>(acctOpenInfoDOList);
        if (page.getList() == null || page.getList().isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "用户列表不存在");
        }
        List<UserListResponse> userListResponseList = page.getList().stream()
                .map(i -> new UserListResponse(i.getUserCode(), i.getInfoCode(), i.getName(),
                        redisService.getDataDictionary("ID_TYPE", tabCode, i.getIdType()), i.getIdCode(),
                        redisService.getOrganizationName(i.getOrgCode()), i.getEmail()))
                .collect(Collectors.toList());
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("userList", userListResponseList);
        data.put("pageSize", Integer.parseInt(redisService.getParaValue("pageSize")));
        return data;
    }

    /**
     * 获取用户审核状态
     *
     * @param userCode
     * @return
     */
    @Override
    public Map<String, Object> getUserAndState(int userCode) {
        AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByUserCode(userCode);
        CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userCode);
        Map<String, Object> data = new LinkedHashMap<>();
        if (acctOpenInfoDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "获取用户开户信息失败");
        }
        data.put("infoStatus", redisService.getDataDictionary("INFO_STATUS", "acct_open_info", acctOpenInfoDO.getInfoStatus()));
        data.put("auditRemark", acctOpenInfoDO.getAuditRemark());
        if (custAcctDO == null) {
            data.put("custCode", null);
        } else {
            data.put("custCode", custAcctDO.getCustCode());
        }
        return data;
    }

    /**
     * 如果已存在开户资料修改原先的开户资料
     * 如果不存在开户资料则增加数据项
     *
     * @param userCode
     * @param acctOpenInfoDO
     * @param imageDO
     */
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
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(acctOpenInfoDO.getAnsOne());
        stringBuffer.append(acctOpenInfoDO.getAnsTwo());
        stringBuffer.append(acctOpenInfoDO.getAnsThree());
        stringBuffer.append(acctOpenInfoDO.getAnsFour());
        stringBuffer.append(acctOpenInfoDO.getAnsFive());
        stringBuffer.append(acctOpenInfoDO.getAnsSix());
        stringBuffer.append(acctOpenInfoDO.getAnsSeven());
        stringBuffer.append(acctOpenInfoDO.getAnsEight());
        stringBuffer.append(acctOpenInfoDO.getAnsNine());
        stringBuffer.append(acctOpenInfoDO.getAnsTen());
        char[] s = stringBuffer.toString().toCharArray();
        GetPoint getPoint = new GetPoint(s);
        acctOpenInfoDO.setRiskScore(getPoint.getPoint());
        acctOpenInfoDO.setInvestorType(getPoint.getInvestorType());
        //System.out.println(acctOpenInfoDO.toString());
        if (acctOpenInfoDO1 != null) {
            if (acctOpenInfoDO1.getInfoCode() == 1) {
                throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "已开户不能重复提交");
            } else {
                acctOpenInfoDO.setInfoStatus("0");
                affectRow = acctOpenInfoDOMapper.updateByPrimaryKeySelective(acctOpenInfoDO);
            }
        } else {
            affectRow = acctOpenInfoDOMapper.insertSelective(acctOpenInfoDO);
        }
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "添加开户资料失败");
        }

    }

    /**
     * 获取开户资料
     *
     * @param userCode
     * @return
     */
    @Override
    public Map<String, Object> getAccountOpeningInfo(int userCode) {
        String tabCode = "acct_open_info";
        AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByUserCode(userCode);

        if (acctOpenInfoDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "未提交开户资料");
        }
        ImageDO imageDO = imageDOMapper.selectByPrimaryKey(acctOpenInfoDO.getImgCode());
        if (imageDO == null) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "未提交影像资料");
        }

        String orgName = redisService.getOrganizationName(acctOpenInfoDO.getOrgCode());
        String gender = redisService.getDataDictionary("GENDER", tabCode, acctOpenInfoDO.getGender());
        String idType = redisService.getDataDictionary("ID_TYPE", tabCode, acctOpenInfoDO.getIdType());
        String education = redisService.getDataDictionary("EDUCATION", tabCode, acctOpenInfoDO.getEducation());
        String bankType = redisService.getDataDictionary("BANK_TYPE", tabCode, acctOpenInfoDO.getBankType());
        String openChannel = redisService.getDataDictionary("OPEN_CHANNEL", tabCode, acctOpenInfoDO.getOpenChannel());
        String infoStatus = redisService.getDataDictionary("INFO_STATUS", tabCode, acctOpenInfoDO.getInfoStatus());
        String investorType = redisService.getDataDictionary("INVESTOR_TYPE", tabCode, acctOpenInfoDO.getInvestorType());
        String stkEx = redisService.getDataDictionary("STK_EX", tabCode, acctOpenInfoDO.getStkEx());
        String stkBd = redisService.getDataDictionary("STK_BD", tabCode, acctOpenInfoDO.getStkBd());
        Map<String, Object> data = objectMapper.convertValue(acctOpenInfoDO, Map.class);
        data.put("orgName", orgName);
        data.put("genderName", gender);
        data.put("idTypeName", idType);
        data.put("educationName", education);
        data.put("bankTypeName", bankType);
        data.put("openChannelName", openChannel);
        data.put("infoStatusName", infoStatus);
        data.put("investorTypeName", investorType);
        data.put("stkExName", stkEx);
        data.put("stkBdName", stkBd);
        data.put("idFront", imageDO.getIdFront());
        data.put("idBack", imageDO.getIdBack());
        data.put("face", imageDO.getFace());
        CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userCode);
        if(custAcctDO==null){
            data.put("custCode",null);
        }else{
            data.put("custCode",custAcctDO.getCustCode());
        }
        return data;
    }

    /**
     * 获取数据字典里面的数据列表，如银行列表等
     *
     * @param colCode 列名
     * @param tabCode 表名
     * @return
     */
    @Override
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

    /**
     * 从身份证号码中获取生日
     *
     * @param idCode
     * @return
     */
    public Long getBirthDayFromIdCode(String idCode) {
        Date date;
        DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        if (idCode.length() == 18) {
            //System.out.println(idCode.substring(6, 14));
            try {
                date = simpleDateFormat.parse(idCode.substring(6, 14));
                DateTime dateTime = formatter.parseDateTime("11111111");
                System.out.println(dateTime.getMillis());
            } catch (ParseException e) {
                return 0L;
            }
            return date.getTime();
        }
        return 0L;
    }

    private Date getNeedTime(int hour, int minute, int second, int addDay, int... args) {
        Calendar calendar = Calendar.getInstance();
        if (addDay != 0) {
            calendar.add(Calendar.DATE, addDay);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, second);
        if (args.length == 1) {
            calendar.set(Calendar.MILLISECOND, args[0]);
        }
        return calendar.getTime();
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

    /*@Override
    public Map<String,Object> getUserAccountInfo(int userCode) {
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
        return data;
    }*/
    /*
    //全部客户
    @Override
    public Map<String, Object> getList(int pageNum) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
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
    }*/
}

