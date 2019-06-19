package com.kht.backend.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class UserPrincipal implements UserDetails {
    //userType为0时 username为telephone ，为1时 username 为employeeCode
    private String username;
    @JsonIgnore
    private String password;
    //userType 0 员工 1 用户
    private int userType;
    //权限
    private  Collection<? extends  GrantedAuthority> authorities;
    //为员工时userCode为employeeCode，为用户时uerCode为custCode
    private String userCode;
    public UserPrincipal(String username, String password, int userType, Collection<? extends GrantedAuthority> authorities, String userCode) {
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.authorities = authorities;
        this.userCode = userCode;
    }
    public static UserPrincipal create(UserDO userDO){
        //TODO
        List<GrantedAuthority> authorities=null;
        return new UserPrincipal(
                userDO.getTelephone().toString(),
                userDO.getPassword(),
                1,
                authorities,
                "");
    }
    public static UserPrincipal create(EmployeeDO employeeDO){
        //TODO
        List<GrantedAuthority>authorities=null;
        return new UserPrincipal(
                employeeDO.getEmployeeCode(),
                employeeDO.getEmployeePwd(),
                0,
                authorities,
                employeeDO.getEmployeeCode());
    }
    public static UserPrincipal create(Map<String,Object> claims){
        String username=(String)claims.get("username");

        return new UserPrincipal(
                (String)username,
                (String)claims.get("password"),//TODO
                0,
                (List<GrantedAuthority>)claims.get("authorities"),
                (String)claims.get("userCode"));
    }
    public Map<String,Object> convertToMap(){
        Map<String,Object>claims=new HashMap<>();
        claims.put("username",this.username);
        //claims.put("password",this.password);
        claims.put("userType",this.userType);
        claims.put("authorities",this.authorities);
        claims.put("userCode",this.userCode);
        return claims;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public String getUsername() {
        return username;
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipal that = (UserPrincipal) o;
        return Objects.equals(username, that.username);
    }
    @Override
    public int hashCode() {

        return Objects.hash(username);
    }


}
