package com.kht.backend.service.impl;

import com.kht.backend.dao.*;
import com.kht.backend.dataobject.*;
import com.kht.backend.service.model.UserGrantedAuthority;
import com.kht.backend.service.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserPrincipalServiceImpl implements UserDetailsService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private EmployeeDOMapper employeeDOMapper;
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;
    @Autowired
    private OperationDOMapper operationDOMapper;
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;

    /**
     * 从数据库读取用户
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Long telephone=Long.parseLong(username);
        UserDO userDO=userDOMapper.selectByTelephone(telephone);
        if(userDO==null){
            throw new UsernameNotFoundException("User not found with username or email : " + userDO.getUserCode());
        }
        List<GrantedAuthority> authorities;
        String code=null;
        switch (userDO.getUserType()) {
            case "0": {
                CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userDO.getUserCode());
                int posCode=0;
                if (custAcctDO != null) {
                    code=custAcctDO.getCustCode();
                }
                AcctOpenInfoDO acctOpenInfoDO=acctOpenInfoDOMapper.selectByUserCode(userDO.getUserCode());
                if(acctOpenInfoDO==null){
                    posCode=8;
                } else {
                    if(acctOpenInfoDO.getInfoStatus().equals("1")){
                    posCode=9; //8 待审核 9 审核通过
                }
                    if(acctOpenInfoDO.getInfoStatus().equals("0")||acctOpenInfoDO.equals("2")) {
                        posCode = 8;
                    }
                }
                if(posCode==0){
                    throw new UsernameNotFoundException("empty posCode");
                }
                List<OperationDO> operationDOList=operationDOMapper.selectByPosition(posCode);
                authorities = operationDOList.stream()
                        .filter(operationDO -> operationDO != null && operationDO.getUrl() != null)
                        .map(operationDO -> new UserGrantedAuthority(operationDO.getUrl(), operationDO.getOperaType()))
                        .collect(Collectors.toList());
                break;
            }
            case "1": {
                EmployeeDO employeeDO = employeeDOMapper.selectByUserCode(userDO.getUserCode());
                if (employeeDO != null) {
                    code = employeeDO.getEmployeeCode();
                    List<OperationDO> operationDOList = operationDOMapper.selectByPosition(employeeDO.getPosCode());
                    authorities = operationDOList.stream()
                            .filter(operationDO -> operationDO != null && operationDO.getUrl() != null)
                            .map(operationDO -> new UserGrantedAuthority(operationDO.getUrl(), operationDO.getOperaType()))
                            .collect(Collectors.toList());
                }
                else{
                    throw new UsernameNotFoundException("user :"+userDO.getUserCode()+" can not found");
                }
                break;
            }
            default:
                throw new UsernameNotFoundException("UserType Error");
        }
        UserPrincipal userPrincipal=UserPrincipal.create(userDO, code, authorities);
        return userPrincipal;
    }
}
