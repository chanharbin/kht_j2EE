package com.kht.backend.service.impl;

import com.kht.backend.dao.CapAcctDOMapper;
import com.kht.backend.dao.CustAcctDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dao.UserDOMapper;
import com.kht.backend.dataobject.CustAcctDO;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.service.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

public class UserPrincipalService implements UserDetailsService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private EmployeeDOMapper employeeDOMapper;
    @Autowired
    private CustAcctDOMapper custAcctDOMapper;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            Long telephone=Long.parseLong(username);
            UserDO userDO=userDOMapper.selectByTelephone(telephone);
            if(userDO==null){
                throw new UsernameNotFoundException("User not found with username or email : " + userDO.getUserCode());
            }
            List<GrantedAuthority> authorities=new ArrayList<>();
            switch (userDO.getUserType()) {
                case "0":
                    CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userDO.getUserCode());
                    if (custAcctDO == null) {
                        throw new UsernameNotFoundException("User not found with userCode : " + userDO.getUserCode());
                    }
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    return UserPrincipal.create(userDO, custAcctDO.getCustCode(), authorities);
                case "1":
                    EmployeeDO employeeDO = employeeDOMapper.selectByUserCode(userDO.getUserCode());
                    if (employeeDO == null) {
                        throw new UsernameNotFoundException("User not found with userCode : " + userDO.getUserCode());
                    }
                    //TODO 权限待改
                    return UserPrincipal.create(userDO, employeeDO.getEmployeeCode(), authorities);
                default:
                    throw new UsernameNotFoundException("UserType Error");
            }
    }
}
