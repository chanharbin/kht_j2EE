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
import com.kht.backend.security.MD5PasswordEncoder;
import com.kht.backend.service.AccountService;
import com.kht.backend.service.model.CapitalAccountInfoResponse;
import com.kht.backend.util.IdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;
    @Autowired
    private CapAcctDOMapper capAcctDOMapper;
    @Autowired
    private DepAcctDOMapper depAcctDOMapper;
    @Autowired
    private TrdAcctDOMapper trdAcctDOMapper;
    @Autowired
    private IdProvider idProvider;
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private MD5PasswordEncoder md5PasswordEncoder;
    @Autowired
    private RedisServiceImpl redisService;

    //用于将Object 转成 Map <String,Object>
    private ObjectMapper objectMapper = new ObjectMapper();
    @Value("${app.pageSize}")
    private int pageSize;

    /**
     * 新增客户账户
     *
     * @param custAcctDO
     * @return 客户账户主码
     */
    @Override
    @Transactional
    public String increaseCustomerAccount(CustAcctDO custAcctDO) {
        custAcctDO.setCustCode(idProvider.getId(custAcctDO.getOrgCode()));
        int affectRow = custAcctDOMapper.insertSelective(custAcctDO);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "开启客户账户失败");
        }
        return custAcctDO.getCustCode();
    }

    /**
     * 获取客户账户信息
     *
     * @param customerCode
     * @return
     */
    @Override
    public Map<String, Object> getCustomerAccountByCustCode(String customerCode) {
        CustAcctDO custAcctDO = custAcctDOMapper.selectByPrimaryKey(customerCode);
        if (custAcctDO == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "客户账户不存在");
        }
        Map<String, Object> custAcctMap = objectMapper.convertValue(custAcctDO, Map.class);
        String tabCode = "cust_acct";
        custAcctMap.put("gender", redisService.getDataDictionary("GENDER", tabCode, (String) custAcctMap.get("gender")));
        custAcctMap.put("idType", redisService.getDataDictionary("ID_TYPE", tabCode, (String) custAcctMap.get("idType")));
        custAcctMap.put("education", redisService.getDataDictionary("EDUCATION", tabCode, (String) custAcctMap.get("education")));
        custAcctMap.put("investorType", redisService.getDataDictionary("INVESTOR_TYPE", tabCode, (String) custAcctMap.get("investorType")));
        custAcctMap.put("custStatus", redisService.getDataDictionary("CUST_STATUS", tabCode, (String) custAcctMap.get("custStatus")));
        custAcctMap.put("birthday",getBirthDayFromIdCode(custAcctDO.getIdCode()));
        return custAcctMap;
    }

    public Map<String, Object> getAllCustomerAccount(int pageNum) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<CustAcctDO> custAcctDOList = custAcctDOMapper.listAll();
        if (custAcctDOList == null || custAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "客户列表不存在");
        }
        PageInfo<CustAcctDO> page = new PageInfo<>(custAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("customerAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> getCustomerAccountByOrgCode(int pageNum, String orgCode) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<CustAcctDO> custAcctDOList = custAcctDOMapper.selectCustCodeByOrgCode(orgCode);
        if (custAcctDOList == null || custAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "客户列表不存在");
        }
        PageInfo<CustAcctDO> page = new PageInfo<>(custAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("customerAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> getCustomerAccountByOpenTime(int pageNum, long startTime, long endTime) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<CustAcctDO> custAcctDOList = custAcctDOMapper.selectByOpenTime(startTime, endTime);
        if (custAcctDOList == null || custAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "客户列表不存在");
        }
        PageInfo<CustAcctDO> page = new PageInfo<>(custAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("customerAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }

    /**
     * 新增资金账户
     *
     * @param customerCode
     * @param capitalAccountPassword
     * @return 资金账户自增主码
     */
    @Override
    @Transactional
    public String increaseCapitalAccount(String customerCode, String capitalAccountPassword) {
        CustAcctDO custAcctDO = custAcctDOMapper.selectByPrimaryKey(customerCode);
        if (custAcctDO == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "客户账户不存在");
        }
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByCustomerCode(customerCode);
        CapAcctDO capAcctDO = new CapAcctDO();
        capAcctDO.setCapCode(idProvider.getId(custAcctDO.getOrgCode()));
        capAcctDO.setCustCode(customerCode);
        capAcctDO.setOrgCode(custAcctDO.getOrgCode());
        capAcctDO.setCapPwd(md5PasswordEncoder.encode(capitalAccountPassword));
        capAcctDO.setCurrency("0");
        capAcctDO.setAttr("0");
        capAcctDO.setOpenTime(new Date().getTime());
        capAcctDO.setCloseTime(-1L);
        capAcctDO.setCapStatus("0");
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            capAcctDO.setMainFlag(true);
        }
        int affectRow = capAcctDOMapper.insertSelective(capAcctDO);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "增加资金账户失败");
        }
        return capAcctDO.getCapCode();
    }

    /**
     * 获取资金账户信息
     *
     * @param customerCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getCapitalAccountByCustCode(String customerCode) {
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByCustomerCode(customerCode);
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "资金账户不存在");
        }
        List<Map<String, Object>> mapList = capAcctDOList.stream()
                .map(capAcctDO -> {
                    return (Map<String, Object>) objectMapper.convertValue(capAcctDO, Map.class);
                })
                .collect(Collectors.toList());
        String tabCode = "cap_acct";
        return mapList.stream()
                .filter(capAccMap -> capAccMap != null)
                .map(capAccMap -> {
                    DepAcctDO depAcctDO = depAcctDOMapper.selectByCapCode((String) capAccMap.get("capCode"));
                    String depCode = null;
                    if (depAcctDO != null) {
                        depCode = depAcctDO.getDepCode();
                    }
                    capAccMap.put("orgCode", redisService.getOrganizationName((String) capAccMap.get("orgCode")));
                    capAccMap.put("closeTime", (Long) capAccMap.get("closeTime") <= 0 ? "-" : (Long) capAccMap.get("closeTime"));
                    capAccMap.put("mainFlag", (boolean) capAccMap.get("mainFlag") ? "是" : "否");
                    capAccMap.put("depCode", depCode);
                    capAccMap.put("currency", redisService.getDataDictionary("CURRENCY", tabCode, (String) capAccMap.get("currency")));
                    capAccMap.put("attr", redisService.getDataDictionary("ATTR", tabCode, (String) capAccMap.get("attr")));
                    capAccMap.put("capStatus", redisService.getDataDictionary("CAP_STATUS", tabCode, (String) capAccMap.get("capStatus")));
                    capAccMap.remove("capPwd");
                    return capAccMap;
                })
                .collect(Collectors.toList());
    }

    public Map<String, Object> getAllCapitalAccount(int pageNum) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.listAll();
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "资金列表不存在");
        }
        PageInfo<CapAcctDO> page = new PageInfo<>(capAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("capitalAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> getCapitalAccountByOrgCode(int pageNum, String orgCode) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByOrgCode(orgCode);
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "资金列表不存在");
        }
        PageInfo<CapAcctDO> page = new PageInfo<>(capAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("capitalAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }

    public Map<String, Object> getCapitalAccountByOpenTime(int pageNum, long startTime, long endTime) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByOpenTime(startTime, endTime);
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "资金列表不存在");
        }
        PageInfo<CapAcctDO> page = new PageInfo<>(capAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("capitalAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }

    /**
     * 修改资金账户密码
     *
     * @param capCode
     * @param oldPassword
     * @param newPassword
     */
    @Override
    @Transactional
    public void modifyCapitalAccount(String capCode, String oldPassword, String newPassword) {
        CapAcctDO capAcctDO = capAcctDOMapper.selectByPrimaryKey(capCode);
        if (capAcctDO == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "资金账户不存在");
        }
        if (!md5PasswordEncoder.matches(oldPassword, capAcctDO.getCapPwd())) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "密码错误");
        }
        capAcctDO.setCapPwd(newPassword);
        int affectRow = capAcctDOMapper.updateByPrimaryKeySelective(capAcctDO);
        if (affectRow == 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "修改密码失败");
        }
    }

    /**
     * 获取资金账户列表
     *
     * @param customerCode
     * @return
     */
    public List<CapitalAccountInfoResponse> getCapitalAccountInfo(String customerCode) {
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByCustomerCode(customerCode);
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON, "资金账户不存在");
        }
        List<CapitalAccountInfoResponse> capitalAccountInfoResponseList = capAcctDOList.stream()
                .map(i -> new CapitalAccountInfoResponse(i.getCapCode(), depAcctDOMapper.selectByCapCode(i.getCapCode()).getDepCode(),
                        i.getCurrency(), i.getMainFlag(), i.getAttr(),
                        organizationDOMapper.selectByPrimaryKey(i.getOrgCode()).getOrgName(),
                        i.getOpenTime(), i.getCloseTime(), i.getCapStatus()))
                .collect(Collectors.toList());
        return capitalAccountInfoResponseList;
    }

    /**
     * 新增存管账户
     *
     * @param capitalCode
     * @param bankType
     * @param bankCardCode
     * @return 存管账户主码
     */
    @Override
    @Transactional
    public String increaseDepositoryAccount(String capitalCode, String bankType, String bankCardCode) {
        CapAcctDO capAcctDO = capAcctDOMapper.selectByPrimaryKey(capitalCode);
        if (capAcctDO == null) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "资金账户不存在");
        }
        DepAcctDO depAcctDO = new DepAcctDO();
        depAcctDO.setDepCode(capAcctDO.getCapCode());
        depAcctDO.setCapCode(capitalCode);
        depAcctDO.setBankType(bankType);
        depAcctDO.setBankCardCode(bankCardCode);
        depAcctDO.setOpenTime(new Date().getTime());
        depAcctDO.setCloseTime(-1L);
        int affectRow = depAcctDOMapper.insertSelective(depAcctDO);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "开启存管账户失败");
        }
        return depAcctDO.getDepCode();
    }

    /**
     * 获取存管账户信息
     *
     * @param customerCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getDepositoryAccountByCustCode(String customerCode) {
        List<CapAcctDO> capAcctDOList = capAcctDOMapper.selectByCustomerCode(customerCode);
        if (capAcctDOList == null || capAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "资金账户不存在");
        }
        List<Map<String, Object>> mapList = capAcctDOList.stream()
                .map(i -> (Map<String, Object>) objectMapper.convertValue(depAcctDOMapper.selectByCapCode(i.getCapCode()), Map.class))
                .collect(Collectors.toList());
        if (mapList == null || mapList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "存管账户不存在");
        }
        String tabCode = "dep_acct";
        return mapList.stream()
                .filter(depAccMap -> depAccMap != null)
                .map(depAcctMap -> {
                    depAcctMap.put("bankType", redisService.getDataDictionary("BANK_TYPE", tabCode, (String) depAcctMap.get("bankType")));
                    depAcctMap.put("depStatus", redisService.getDataDictionary("DEP_STATUS", tabCode, (String) depAcctMap.get("depStatus")));
                    return depAcctMap;
                })
                .collect(Collectors.toList());
    }
    public Map<String, Object> getAllDepositoryAccount(int pageNum) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<DepAcctDO> depAcctDOList = depAcctDOMapper.listAll();
        if (depAcctDOList == null || depAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "存管账户不存在");
        }
        PageInfo<DepAcctDO> page = new PageInfo<>(depAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("depositoryAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }
    public Map<String, Object> getDepositoryAccountByOrgCode(int pageNum,String orgCode) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<DepAcctDO> depAcctDOList = depAcctDOMapper.selectByOrgCode(orgCode);
        if (depAcctDOList == null || depAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "存管账户不存在");
        }
        PageInfo<DepAcctDO> page = new PageInfo<>(depAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("depositoryAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }
    public Map<String, Object> getDepositoryAccountByOpenTime(int pageNum, long startTime, long endTime) {
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<DepAcctDO> depAcctDOList = depAcctDOMapper.selectByOpenTime(startTime,endTime);
        if (depAcctDOList == null || depAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "存管账户不存在");
        }
        PageInfo<DepAcctDO> page = new PageInfo<>(depAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("depositoryAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }
    /**
     * 新增证券账户信息
     *
     * @param customerCode
     * @param stkEx
     * @param stkBd
     * @param custType
     * @param trdUnit
     * @return 证券账户主码
     */
    @Override
    @Transactional
    public String increaseTradeAccount(String customerCode, String stkEx, String stkBd,
                                       String custType, String trdUnit) {
        TrdAcctDO trdAcctDO = new TrdAcctDO();
        trdAcctDO.setTrdCode(idProvider.getTrdId(stkBd));
        trdAcctDO.setCustCode(customerCode);
        trdAcctDO.setCustType(custType);
        trdAcctDO.setStkBd(stkBd);
        trdAcctDO.setStkEx(stkEx);
        trdAcctDO.setTrdUnit(trdUnit);
        trdAcctDO.setOpenTime(new Date().getTime());
        trdAcctDO.setCloseTime(-1L);
        trdAcctDO.setTdrStatus("0");
        int affectRow = trdAcctDOMapper.insertSelective(trdAcctDO);
        if (affectRow <= 0) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "证券账户插入");
        }
        return trdAcctDO.getTrdCode();
    }
    /**
     * 获取证券账户信息
     *
     * @param customerCode
     * @return
     */
    @Override
    public List<Map<String, Object>> getTradeAccount(String customerCode) {
        List<TrdAcctDO> trdAcctDOList = trdAcctDOMapper.selectByCustomerCode(customerCode);
        if (trdAcctDOList == null || trdAcctDOList.isEmpty()) {
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "证券账户不存在");
        }
        List<Map<String, Object>> mapList = trdAcctDOList.stream()
                .map(capAcctDO -> (Map<String, Object>) objectMapper.convertValue(capAcctDO, Map.class))
                .collect(Collectors.toList());
        String tabCode = "trd_acct";
        return mapList.stream()
                .filter(trdAccMap -> trdAccMap != null)
                .map(trdAccMap -> {
                    trdAccMap.put("stkEx", redisService.getDataDictionary("STK_EX", tabCode, (String) trdAccMap.get("stkEx")));
                    trdAccMap.put("stkBd", redisService.getDataDictionary("STK_BD", tabCode, (String) trdAccMap.get("stkBd")));
                    trdAccMap.put("custType", redisService.getDataDictionary("CUST_TYPE", tabCode, (String) trdAccMap.get("custType")));
                    trdAccMap.put("trdUnit", redisService.getDataDictionary("TRD_UNIT", tabCode, (String) trdAccMap.get("trdUnit")));
                    trdAccMap.put("tdrStatus", redisService.getDataDictionary("TDR_STATUS", tabCode, (String) trdAccMap.get("tdrStatus")));
                    return trdAccMap;
                })
                .collect(Collectors.toList());
    }

    public Map<String,Object>getAllTradeAccount(int pageNum){
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.listAll();
        if(trdAcctDOList==null|trdAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "证券账户不存在");
        }
        PageInfo<TrdAcctDO> page = new PageInfo<>(trdAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("tradeAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }
    public Map<String,Object>getTradeAccountByOrgCode(int pageNum,String orgCode){
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByOrgCode(orgCode);
        if(trdAcctDOList==null|trdAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "证券账户不存在");
        }
        PageInfo<TrdAcctDO> page = new PageInfo<>(trdAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("tradeAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }
    public Map<String,Object>getTradeAccountByOpenTime(int pageNum, long startTime, long endTime){
        Page<Object> objectPage = PageHelper.startPage(pageNum, pageSize);
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByOpenTime(startTime,endTime);
        if(trdAcctDOList==null|trdAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION, "证券账户不存在");
        }
        PageInfo<TrdAcctDO> page = new PageInfo<>(trdAcctDOList);
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("totalNum", page.getTotal());
        data.put("tradeAccountList", page.getList());
        data.put("pageSize", pageSize);
        return data;
    }
    private Long getBirthDayFromIdCode(String idCode) {
        Date date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        if (idCode.length() == 18) {
            System.out.println(idCode.substring(6, 14));
            try {
                date = simpleDateFormat.parse(idCode.substring(6, 14));
            } catch (ParseException e) {
                return 0L;
            }
            return date.getTime();
        }
        return 0L;
    }
}
