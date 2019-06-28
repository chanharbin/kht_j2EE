package com.kht.backend.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kht.backend.dao.*;
import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.security.MD5PasswordEncoder;
import com.kht.backend.service.AccountService;
import com.kht.backend.service.model.CapitalAccountInfoResponse;
import com.kht.backend.util.IdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    //用于Object to Map String
    private ObjectMapper objectMapper = new ObjectMapper();

    //新增客户账户
    @Override
    public String increaseCustomerAccount(CustAcctDO custAcctDO) {
        custAcctDO.setCustCode(idProvider.getId(custAcctDO.getOrgCode()));
        int affectRow = custAcctDOMapper.insertSelective(custAcctDO);
        if(affectRow <= 0 ){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"开启客户账户失败");
        }
        return custAcctDO.getCustCode();
    }
    @Override
    public Map<String,Object> getCustomerAccount(String customerCode) {
        CustAcctDO custAcctDO=custAcctDOMapper.selectByPrimaryKey(customerCode);
        if(custAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"客户账户不存在");
        }
        Map<String,Object> custAcctMap=objectMapper.convertValue(custAcctDO,Map.class);
        String tabCode="cust_acct";
        custAcctMap.put("gender",redisService.getDataDictionary("GENDER",tabCode,(String)custAcctMap.get("gender")));
        custAcctMap.put("idType",redisService.getDataDictionary("ID_TYPE",tabCode,(String)custAcctMap.get("idType")));
        custAcctMap.put("education",redisService.getDataDictionary("EDUCATION",tabCode,(String)custAcctMap.get("education")));
        custAcctMap.put("investorType",redisService.getDataDictionary("INVESTOR_TYPE",tabCode,(String)custAcctMap.get("investorType")));
        custAcctMap.put("custStatus",redisService.getDataDictionary("CUST_STATUS",tabCode,(String)custAcctMap.get("custStatus")));
        return custAcctMap;
    }
    //新增资金账户
    @Override
    public String increaseCapitalAccount(String customerCode,String capitalAccountPassword) {
        CustAcctDO custAcctDO=custAcctDOMapper.selectByPrimaryKey(customerCode);
        if(custAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"客户账户不存在");
        }
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        CapAcctDO capAcctDO=new CapAcctDO();
        capAcctDO.setCapCode(idProvider.getId(custAcctDO.getOrgCode()));
        capAcctDO.setCustCode(customerCode);
        capAcctDO.setOrgCode(custAcctDO.getOrgCode());
        capAcctDO.setCapPwd(md5PasswordEncoder.encode(capitalAccountPassword));
        capAcctDO.setCurrency("0");
        capAcctDO.setAttr("0");
        capAcctDO.setOpenTime(new Date().getTime());
        capAcctDO.setCloseTime(-1L);
        capAcctDO.setCapStatus("0");
        if(capAcctDOList==null||capAcctDOList.isEmpty()){
            capAcctDO.setMainFlag(true);
        }
        int affectRow = capAcctDOMapper.insertSelective(capAcctDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"增加资金账户失败");
        }
        return capAcctDO.getCapCode();
    }

    @Override
    public List<Map<String,Object>> getCapitalAccount(String customerCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        if(capAcctDOList==null||capAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        List<Map<String,Object>> mapList=capAcctDOList.stream()
                .map(capAcctDO -> {
                    return (Map<String,Object>)objectMapper.convertValue(capAcctDO,Map.class);})
                .collect(Collectors.toList());
        String tabCode="cap_acct";
        return mapList.stream()
                .filter(capAccMap->capAccMap!=null)
                .map(capAccMap->{
                    DepAcctDO depAcctDO=depAcctDOMapper.selectByCapCode((String)capAccMap.get("capCode"));
                    String depCode=null;
                    if(depAcctDO!=null){
                        depCode=depAcctDO.getDepCode();
                    }
                    capAccMap.put("orgCode",redisService.getOrganizationName((String)capAccMap.get("orgCode")));
                    capAccMap.put("closeTime",(Long)capAccMap.get("closeTime")<=0?"-":(Long)capAccMap.get("closeTime"));
                    capAccMap.put("mainFlag",(boolean)capAccMap.get("mainFlag")?"是":"否");
                    capAccMap.put("depCode",depCode);
                    capAccMap.put("currency",redisService.getDataDictionary("CURRENCY",tabCode,(String)capAccMap.get("currency")));
                    capAccMap.put("attr",redisService.getDataDictionary("ATTR",tabCode,(String)capAccMap.get("attr")));
                    capAccMap.put("capStatus",redisService.getDataDictionary("CAP_STATUS",tabCode,(String)capAccMap.get("capStatus")));
                    capAccMap.remove("capPwd");
                    return capAccMap;
                })
                .collect(Collectors.toList());
    }

    //新增存管账户
    @Override
    public String increaseDepositoryAccount(String capitalCode,String bankType,String bankCardCode) {
        CapAcctDO capAcctDO=capAcctDOMapper.selectByPrimaryKey(capitalCode);
        if(capAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        DepAcctDO depAcctDO=new DepAcctDO();
        depAcctDO.setDepCode(capAcctDO.getCapCode());
        depAcctDO.setCapCode(capitalCode);
        depAcctDO.setBankType(bankType);
        depAcctDO.setBankCardCode(bankCardCode);
        depAcctDO.setOpenTime(new Date().getTime());
        depAcctDO.setCloseTime(-1L);
        int affectRow = depAcctDOMapper.insertSelective(depAcctDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"开启存管账户失败");
        }
        return depAcctDO.getDepCode();
    }

    @Override
    public List<Map<String,Object>> getDepositoryAccount(String customerCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        if(capAcctDOList==null||capAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        List<Map<String,Object>> mapList=capAcctDOList.stream()
                .map(i-> (Map<String,Object>)objectMapper.convertValue(depAcctDOMapper.selectByCapCode(i.getCapCode()),Map.class))
                .collect(Collectors.toList());
        if(mapList==null||mapList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"存管账户不存在");
        }
        String tabCode="dep_acct";
        return mapList.stream()
                .filter(depAccMap->depAccMap!=null)
                .map(depAcctMap->{
                    depAcctMap.put("bankType",redisService.getDataDictionary("BANK_TYPE",tabCode,(String)depAcctMap.get("bankType")));
                    depAcctMap.put("depStatus",redisService.getDataDictionary("DEP_STATUS",tabCode,(String)depAcctMap.get("depStatus")));
                    return depAcctMap;
                })
                .collect(Collectors.toList());
    }

    @Override
    public String increaseTradeAccount(String customerCode,String stkEx,String stkBd,
                                     String custType,String trdUnit) {
        TrdAcctDO trdAcctDO=new TrdAcctDO();
        trdAcctDO.setTrdCode(idProvider.getId(stkBd));
        trdAcctDO.setCustCode(customerCode);
        trdAcctDO.setCustType(custType);
        trdAcctDO.setStkBd(stkBd);
        trdAcctDO.setStkEx(stkEx);
        trdAcctDO.setTrdUnit(trdUnit);
        trdAcctDO.setOpenTime(new Date().getTime());
        trdAcctDO.setCloseTime(-1L);
        trdAcctDO.setTdrStatus("0");
        int affectRow=trdAcctDOMapper.insertSelective(trdAcctDO);
        if(affectRow<=0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"证券账户插入");
        }
        return trdAcctDO.getTrdCode();
    }

    @Override
    public List<Map<String,Object>> getTradeAccount(String customerCode) {
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByCustomerCode(customerCode);
        if(trdAcctDOList==null||trdAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"证券账户不存在");
        }
        List<Map<String,Object>> mapList=trdAcctDOList.stream()
                .map(capAcctDO ->  (Map<String,Object>)objectMapper.convertValue(capAcctDO,Map.class))
                .collect(Collectors.toList());
        String tabCode="trd_acct";
        return mapList.stream()
                .filter(trdAccMap->trdAccMap!=null)
                .map(trdAccMap->{
                    trdAccMap.put("stkEx",redisService.getDataDictionary("STK_EX",tabCode,(String)trdAccMap.get("stkEx")));
                    trdAccMap.put("stkBd",redisService.getDataDictionary("STK_BD",tabCode,(String)trdAccMap.get("stkBd")));
                    trdAccMap.put("custType",redisService.getDataDictionary("CUST_TYPE",tabCode,(String)trdAccMap.get("custType")));
                    trdAccMap.put("trdUnit",redisService.getDataDictionary("TRD_UNIT",tabCode,(String)trdAccMap.get("trdUnit")));
                    trdAccMap.put("tdrStatus",redisService.getDataDictionary("TDR_STATUS",tabCode,(String)trdAccMap.get("tdrStatus")));
                    return trdAccMap;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void modifyCapitalAccount(String capCode, String oldPassword, String newPassword) {
        CapAcctDO capAcctDO=capAcctDOMapper.selectByPrimaryKey(capCode);
        if(capAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        if(!md5PasswordEncoder.matches(oldPassword,capAcctDO.getCapPwd())){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"密码错误");
        }
        capAcctDO.setCapPwd(newPassword);
        int affectRow=capAcctDOMapper.updateByPrimaryKeySelective(capAcctDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改密码失败");
        }
    }
    public List<CapitalAccountInfoResponse> getCapitalAccountInfo(String customerCode){
        List<CapAcctDO> capAcctDOList= capAcctDOMapper.selectByCustomerCode(customerCode);
        if(capAcctDOList==null||capAcctDOList.isEmpty()){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"资金账户不存在");
        }
        List<CapitalAccountInfoResponse> capitalAccountInfoResponseList =capAcctDOList.stream()
                .map(i->new CapitalAccountInfoResponse(i.getCapCode(),depAcctDOMapper.selectByCapCode(i.getCapCode()).getDepCode(),
                        i.getCurrency(),i.getMainFlag(),i.getAttr(),
                        organizationDOMapper.selectByPrimaryKey(i.getOrgCode()).getOrgName(),
                        i.getOpenTime(),i.getCloseTime(),i.getCapStatus()))
                .collect(Collectors.toList());
        return capitalAccountInfoResponseList;
    }
}
