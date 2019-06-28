package com.kht.backend.controller;

import com.kht.backend.dataobject.*;
import com.kht.backend.entity.Result;
import com.kht.backend.exception.AuthenticationException;
import com.kht.backend.service.impl.AccountServiceImpl;
import com.kht.backend.service.impl.RedisServiceImpl;
import com.kht.backend.service.impl.SystemParameterServiceImpl;
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
    private SystemParameterServiceImpl systemParameterService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private RedisServiceImpl redisService;

    private final String prefix="Bearer ";
    @PostMapping(value = {"/user/login","/employee/login"})
    public Result authenticateUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password,HttpServletResponse httpServletResponse){
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(telephone), password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt=jwtTokenProvider.generateToken(authentication);
            UserPrincipal userPrincipal=jwtTokenProvider.getUserPrincipalFromJWT(jwt);
            Map<String,Object>data=new LinkedHashMap<>();
            data.put("userCode",userPrincipal.getUserCode());
            httpServletResponse.setHeader("Authorization",prefix+jwt);
            return Result.OK(data).build();
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }
    @PostMapping(value = "/user/register")
    public Result registerUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password,@RequestParam("checkCode")int checkCode){
        return userService.userRegister(telephone,checkCode,passwordEncoder.encode(password));
    }
    @PutMapping("/user/password")
    public Result modifyUserPassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,HttpServletResponse httpServletResponse)
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        userService.modifyUserPassword(currentUser.getUserCode(),oldPassword,newPassword);
        return Result.OK("更新用户信息成功").build();
    }
    @GetMapping("/user/account-opening-info")
    public Result getUserAccountOpeningInfo(@RequestParam("userCode")int userCode){
        //UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return Result.OK(userService.getAccountOpeningInfo(userCode)).build();
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
    public Result getCheckCode(@RequestParam("telephone")Long telephone){
        userService.getOtp(telephone);
        return Result.OK("获取验证码成功").build();
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
    @GetMapping("/system-parameter")
    public Result getAllSystemParameter(@RequestParam("pageNum")int pageNum){
        Map<String,Object> map=systemParameterService.getAllSystemParameters(pageNum);
        return Result.OK(map).build();
    }
    @PutMapping("/system-parameter")
    public Result modifySystemParameter(@RequestParam("paraCode")int paraCode,@RequestParam("paraValue")String paraValue){
        systemParameterService.modifySystemParameter(paraCode,paraValue);
        return Result.OK("修改参数成功").build();
    }
}
