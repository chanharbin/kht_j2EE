package com.kht.backend.controller;

import com.kht.backend.dataobject.*;
import com.kht.backend.entity.Result;
import com.kht.backend.exception.AuthenticationException;
import com.kht.backend.service.impl.AccountServiceImpl;
import com.kht.backend.service.model.CapitalAccountInfoResponse;
import com.kht.backend.service.model.UserListResponse;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import com.kht.backend.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public Result authenticateUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password,HttpServletResponse httpServletResponse){
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(telephone, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt=jwtTokenProvider.generateToken(authentication);
            UserPrincipal userPrincipal=jwtTokenProvider.getUserPrincipalFromJWT(jwt);
            Map<String,Object>data=new LinkedHashMap<>();
            //data.put("Authorization",prefix+jwt);
            data.put("userCode",userPrincipal.getUserCode());
            httpServletResponse.setHeader("Authorization",prefix+jwt);
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
    @PutMapping("/user/password")
    public Result modifyUserPassword(@RequestParam("oldPassword")String oldPassowrd,@RequestParam("newPassword")String newPassword)
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.modifyUserPassword(currentUser.getUserCode(),oldPassowrd,newPassword);
    }
    @GetMapping("/user/account-opening-info")
    public Result getUserAccountOpeningInfo(@RequestParam("userCode")int userCode){
        //UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.getAccountOpeningInfo(userCode);
    }
    @PostMapping("/user/account-opening-info")
    public Result setUserAccountInfo(AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO){
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        //System.out.println(acctOpenInfoDO.toString()+imageDO.toString());
        return userService.increaseAccountOpenInfo(currentUser.getUserCode(),acctOpenInfoDO,imageDO);
    }
    @GetMapping("/user/account-opening-info/status")
    public Result getUserAccountOpeningInfoStatus(@RequestParam("userCode")int userCode, HttpServletResponse response){
        //response.setHeader("fuck","fuck");
        return Result.OK(userService.getUserAndState(userCode)).build();
    }

    @GetMapping("/user/check-code")
    public Result getCheckCode(@RequestParam("telephone")String telephone){
        return userService.getOtp(telephone);
    }
    @GetMapping("/bank")
    public Result getBankList(){
        return Result.OK(userService.getAllDataInfoList("BANK_TYPE","acct_open_info")).build();
    }
    @GetMapping("/education")
    public Result getEducationList(){
        return Result.OK(userService.getAllDataInfoList("EDUCATION","acct_open_info")).build();
    }
    @GetMapping("/user/list")
    public Result getUserList(@RequestParam("pageNum")int pageNum){
        Map<String,Object> data=userService.getUserInfoList(pageNum);
        return Result.OK(data).build();
    }
    @GetMapping("/user/info")
    public Result getUserInfo()
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.getUserInfo(currentUser.getUserCode());
    }

    //bankType list
    //xueli list
}
