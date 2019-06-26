package com.kht.backend.controller;

import com.kht.backend.dataobject.*;
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
import java.util.List;
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
    @PutMapping("/user/password")
    public Result modifyUserPassword(@RequestParam("oldPassword")String oldPassowrd,@RequestParam("newPassword")String newPassword)
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.modifyUserPassword(currentUser.getUserCode(),oldPassowrd,newPassword);
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

    @GetMapping("/user")
    public Result getUserInfo()
    {
        UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return userService.getUserInfo(currentUser.getUserCode());
    }
    @GetMapping("/user/customerAccount")
    public Result getUserCustomerAccount(@RequestParam("custCode")String customerCode){
        CustAcctDO custAcctDO=accountService.getCustomerAccount(customerCode);
        return Result.OK(custAcctDO).build();
    }
    @GetMapping("/user/depositoryAccount")
    public Result getUserDepositoryAccount(@RequestParam("custCode")String customerCode){
        List<DepAcctDO>depAcctDOList= accountService.getDepositoryAccount(customerCode);
        return Result.OK(depAcctDOList).build();
    }
    @GetMapping("/user/tradeAccount")
    public Result getUserTradeAccount(@RequestParam("custCode")String customerCode){
        List<TrdAcctDO>  trdAcctDOList=accountService.getTradeAccount(customerCode);
        return Result.OK(trdAcctDOList).build();
    }
    @GetMapping("/user/capitalAccount")
    public Result getUserCapitalAccount(@RequestParam("custCode")String customerCode){
        List<CapAcctDO> capAcctDOList= accountService.getCapitalAccount(customerCode);
        return Result.OK(capAcctDOList).build();
    }
    @PutMapping("/user/capitalAccount")
    public Result modifyUserCapitalAccountPassword(@RequestParam("capCode")String capCode,@RequestParam("oldPassword")String oldPassword,
                                                   @RequestParam("newPassword")String newPassword){
        accountService.modifyCapitalAccount(capCode,oldPassword,newPassword);
        return  Result.OK("修改资金密码成功").build();
    }

    @GetMapping("/checkCode")
    public Result getCheckCode(@RequestParam("telephone")String telephone){
        return userService.getOtp(telephone);
    }
    @GetMapping("/user/accountOpeningInfo/status")
    public Result getUserAccountOpeningInfoStatus(@RequestParam("userCode")int userCode){
        return userService.getState(userCode);
    }
    @PostMapping("/user/capitalAccount")
    public Result addUserCapitalAccount(@RequestParam("capPwd")String capPwd,@RequestParam("bankCardCode")String bankCardCode,
                                        @RequestParam("bankType")String bankType,@RequestParam("custCode")String custCode){
        String capCode=accountService.increaseCapitalAccount(custCode,capPwd);
        accountService.increaseDepositoryAccount(capCode,bankType,bankCardCode);
        return Result.OK("增加资金账户和客户账户成功").build();
    }

}
