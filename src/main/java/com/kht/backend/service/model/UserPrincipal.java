package com.kht.backend.service.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import io.jsonwebtoken.Claims;
//import javafx.util.Pair;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

public class UserPrincipal implements UserDetails {
    //等同于userDO内的属性
    private  int userCode;
    private Long telephone;
    private String password;
    private String userType;
    //为员工时code为employeeCode，为用户时code为custCode
    private String code;
    //权限
    private  Collection<? extends  GrantedAuthority> authorities;
    public UserPrincipal(Integer userCode, Long telephone, String password, String userType, String code, Collection<? extends GrantedAuthority> authorities) {
        this.userCode=userCode;
        this.telephone=telephone;
        this.password=password;
        this.userType = userType;
        this.code = code;
        this.authorities = authorities;
    }
    public static UserPrincipal create(UserDO userDO, String code, Collection<? extends GrantedAuthority> authorities) {
        return new UserPrincipal(
                userDO.getUserCode(),
                userDO.getTelephone(),
                userDO.getPassword(),
                userDO.getUserType(),
                code,
                authorities);
    }
    public static UserPrincipal create(Claims claims){
       List<Map<String,String>> temp=(List<Map<String,String>>)claims.get("authorities");
        /*for(Map<String,String>map:temp){
            for (Map.Entry<String, String> entry: map.entrySet()) {
                System.out.println("key:"+entry.getKey());
                System.out.println("value:"+entry.getValue());
            }
        }*/
        List<GrantedAuthority> authoritiesList=((List<Map<String,String>>)claims.get("authorities")).stream()
                .map(authority-> new UserGrantedAuthority(authority.get("url").trim(),authority.get("operaType")))
                .collect(Collectors.toList());
        //System.out.println(authoritiesList);
        return new UserPrincipal(
                (int)claims.get("userCode"),
                Long.valueOf(((Number)claims.get("telephone")).longValue()),
                (String)claims.get("password"),
                (String)claims.get("userType"),
                (String)claims.get("code"),
                authoritiesList
        );
    }
    public Map<String,Object> convertToMap(){
        Map<String,Object>claims=new HashMap<>();
        claims.put("userCode",userCode);
        claims.put("telephone",telephone);
        claims.put("password",password);
        claims.put("userType",userType);
        claims.put("code",code);
        claims.put("authorities",authorities);
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
        return telephone.toString();
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
        return Objects.equals(userCode, that.userCode);
    }
    @Override
    public int hashCode() {
        return Objects.hash(telephone);
    }


    @Override
    public String toString() {
        return "UserPrincipal{" +
                "userCode=" + userCode +
                ", telephone=" + telephone +
                ", password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", code='" + code + '\'' +
                ", authorities=" + authorities +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public int getUserCode() {
        return userCode;
    }
    public Long getTelephone() {
        return telephone;
    }
    public String getUserType() {
        return userType;
    }
    public String getCode() {
        return code;
    }
    public void setUserType(String userType) {
        this.userType = userType;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }
}