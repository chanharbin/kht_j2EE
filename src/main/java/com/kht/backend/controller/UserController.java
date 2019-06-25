package com.kht.backend.controller;

import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.ImageDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.exception.AuthenticationException;
import com.kht.backend.security.CurrentUser;
import com.kht.backend.service.impl.AccountServiceImpl;
import com.kht.backend.service.model.UserAccountInfo;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import com.kht.backend.service.impl.UserServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private AccountServiceImpl accountService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    private final String prefix="Bearer ";
    @PostMapping("/user/login")
    public Result authenticateUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password){
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(telephone, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt=jwtTokenProvider.generateToken(authentication);
            UserPrincipal userPrincipal=jwtTokenProvider.getUserPrincipalFromJWT(jwt);
            Map<String,Object>data=new LinkedHashMap<>();
            data.put("Authorization",prefix+jwt);
            data.put("userCode",userPrincipal.getUserCode());
            return Result.OK(data).build();
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
    @PostMapping("/user/register")
    public Result registerUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password,@RequestParam("checkCode")String checkCode){
        return userService.userRegister(telephone,checkCode,passwordEncoder.encode(password));
    }
    @GetMapping("/user/accountOpeningInfo")
    public Result getUserAccountOpeningInfo(@RequestParam("userCode")int userCode){
        //UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.getAccountOpeningInfo(userCode);
    }
    @PostMapping("/user/accountOpeningInfo")
    public Result setUserAccountInfo(AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO){
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        //System.out.println(acctOpenInfoDO.toString()+imageDO.toString());
        return userService.increaseAccountOpenInfo(currentUser.getUserCode(),acctOpenInfoDO,imageDO);
    }
    @GetMapping("/user/depositoryAccount")
    public Result getUserDepositoryAccount(@RequestParam("custCode")String custCode){
        return accountService.getDeptAccount(custCode);
    }
    @GetMapping("/user")
    public Result getUserInfo()
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.getUserInfo(currentUser.getUserCode());
    }
    @PutMapping("/user/password")
    public Result modifyUserPassword(@RequestParam("oldPassword")String oldPassowrd,@RequestParam("newPassword")String newPassword)
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        //return userService.modifyUserInfo(currentUser.getUserCode(),passwordEncoder.encode(newPassword));
        return null;
    }
}
