package com.kht.backend.service.impl;

import com.kht.backend.dao.CapAcctDOMapper;
import com.kht.backend.dao.CustAcctDOMapper;
import com.kht.backend.dao.DepAcctDOMapper;
import com.kht.backend.dao.TrdAcctDOMapper;
import com.kht.backend.dataobject.CapAcctDO;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.DepAcctDO;
import com.kht.backend.dataobject.TrdAcctDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.AccountService;
import com.kht.backend.util.IdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
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
    public CustAcctDO getCustomerAccount(String customerCode) {
        CustAcctDO custAcctDO=custAcctDOMapper.selectByPrimaryKey(customerCode);
        if(custAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"客户账户不存在");
        }
        return custAcctDO;
       // increaseCapitalAccount(1,)
    }

    //新增资金账户
    @Override
    public String increaseCapitalAccount(String customerCode,String capitalAccountPassword) {
        CustAcctDO custAcctDO=getCustomerAccount(customerCode);
        List<CapAcctDO> capAcctDOList=getCapitalAccount(customerCode);
        CapAcctDO capAcctDO=new CapAcctDO();
        capAcctDO.setCapCode(idProvider.getId(custAcctDO.getOrgCode()));
        capAcctDO.setCustCode(customerCode);
        capAcctDO.setOrgCode(custAcctDO.getOrgCode());
        capAcctDO.setCapPwd(capitalAccountPassword);
        capAcctDO.setCurrency("0");
        capAcctDO.setAttr("0");
        capAcctDO.setOpenTime(new Date().getTime());
        capAcctDO.setCloseTime(-1L);
        capAcctDO.setCapStatus("0");
        if(capAcctDOList==null){
            capAcctDO.setMainFlag(true);
        }
        int affectRow = capAcctDOMapper.insertSelective(capAcctDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"增加资金账户失败");
        }
        return capAcctDO.getCapCode();
    }

    @Override
    public List<CapAcctDO> getCapitalAccount(String customerCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        if(capAcctDOList==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        return capAcctDOList;
    }

    //新增存管账户
    @Override
    public String increaseDepositoryAccount(String capitalCode,String bankType,String bankCardCode) {
        CapAcctDO capAcctDO=capAcctDOMapper.selectByPrimaryKey(capitalCode);
        if(capAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        DepAcctDO depAcctDO=new DepAcctDO();
        depAcctDO.setDepCode(depAcctDO.getCapCode());
        depAcctDO.setCapCode(capitalCode);
        depAcctDO.setBankType(bankType);
        depAcctDO.setBankCardCode(bankCardCode);
        int affectRow = depAcctDOMapper.insertSelective(depAcctDO);
        if(affectRow <= 0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"开启存管账户失败");
        }
        return depAcctDO.getDepCode();
    }

    @Override
    public List<DepAcctDO> getDepositoryAccount(String customerCode) {
        List<CapAcctDO> capAcctDOList=capAcctDOMapper.selectByCustomerCode(customerCode);
        if(capAcctDOList==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        List<DepAcctDO> depAcctDOList =capAcctDOList.stream()
                .map(i-> depAcctDOMapper.selectByCapCode(i.getCapCode()))
                .collect(Collectors.toList());
        if(depAcctDOList==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"存管账户不存在");
        }
        return depAcctDOList;
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
        if(affectRow>=0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"证券账户插入");
        }
        return trdAcctDO.getTrdCode();
    }

    @Override
    public List<TrdAcctDO> getTradeAccount(String customerCode) {
        List<TrdAcctDO> trdAcctDOList=trdAcctDOMapper.selectByCustomerCode(customerCode);
        if(trdAcctDOList==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"证券账户不存在");
        }
        return trdAcctDOList;
    }

    @Override
    public void modifyCapitalAccount(String capCode, String oldPassword, String newPassword) {
        CapAcctDO capAcctDO=capAcctDOMapper.selectByPrimaryKey(capCode);
        if(capAcctDO==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"资金账户不存在");
        }
        if(!capAcctDO.getCapPwd().equals(oldPassword)){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"密码错误");
        }
        capAcctDO.setCapPwd(newPassword);
        int affectRow=capAcctDOMapper.updateByPrimaryKeySelective(capAcctDO);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改密码失败");
        }
    }
}
