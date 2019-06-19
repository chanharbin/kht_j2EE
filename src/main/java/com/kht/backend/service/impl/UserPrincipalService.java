package com.kht.backend.service.impl;

import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dao.UserDOMapper;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.service.model.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
public class UserPrincipalService implements UserDetailsService {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private EmployeeDOMapper employeeDOMapper;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            Long telephone=Long.parseLong(username);
            UserDO userDO=userDOMapper.selectByTelephone(telephone);
            if(userDO==null){
                throw new UsernameNotFoundException("用户不存在 "+username);
            }
            return UserPrincipal.create(userDO);
        }catch (NumberFormatException e){
            EmployeeDO employeeDO=employeeDOMapper.selectByPrimaryKey(username);
            if(employeeDO==null){
                throw new UsernameNotFoundException("员工不存在 "+username);
            }
            return UserPrincipal.create(employeeDO);
        }
    }


}
