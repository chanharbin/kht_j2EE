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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class UserPrincipalServiceImpl implements UserDetailsService {
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
        String code=null;
        switch (userDO.getUserType()) {
            case "0": {
                CustAcctDO custAcctDO = custAcctDOMapper.selectByUserCode(userDO.getUserCode());
                if (custAcctDO != null) {
                    code=custAcctDO.getCustCode();
                }
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                break;
            }
            case "1": {
                EmployeeDO employeeDO = employeeDOMapper.selectByUserCode(userDO.getUserCode());
                if (employeeDO != null) {
                    code=employeeDO.getEmployeeCode();
                }
                authorities.add(new SimpleGrantedAuthority("ROLE_EMPLOYEE"));
                //TODO 权限待改
                break;
            }
            default:
                throw new UsernameNotFoundException("UserType Error");}
        UserPrincipal userPrincipal=UserPrincipal.create(userDO, code, authorities);
        //System.out.println(userPrincipal.toString());
        return userPrincipal;
    }
}
