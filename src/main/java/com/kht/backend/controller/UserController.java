package com.kht.backend.controller;

import com.kht.backend.aspect.MethodLog;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dataobject.*;
import com.kht.backend.entity.Result;
import com.kht.backend.exception.AuthenticationException;
import com.kht.backend.service.EmployeeService;
import com.kht.backend.service.impl.RedisServiceImpl;
import com.kht.backend.service.impl.SystemParameterServiceImpl;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import com.kht.backend.service.impl.UserServiceImpl;
import org.apache.ibatis.annotations.Param;
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
import java.util.HashMap;
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
    private SystemParameterServiceImpl systemParameterService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private RedisServiceImpl redisService;
    @Autowired
    private EmployeeDOMapper employeeDOMapper;

    private final String prefix = "Bearer ";

    @PostMapping(value = {"/user/login", "/employee/login"})
    public Result authenticateUser(@RequestParam("telephone") Long telephone, @RequestParam("password") String password, HttpServletResponse httpServletResponse) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(telephone), password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtTokenProvider.generateToken(authentication);
            UserPrincipal userPrincipal = jwtTokenProvider.getUserPrincipalFromJWT(jwt);
            Map<String, Object> data = new LinkedHashMap<>();
            if (userPrincipal.getUserType().equals("0")) {
                data.put("userCode", userPrincipal.getUserCode());
            } else {
                EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(userPrincipal.getCode());
                String employeeName = employeeDO.getEmployeeName();
                String position = redisService.getPosName(employeeDO.getPosCode());
                data.put("employeeName", employeeName);
                data.put("employeePosition", position);
            }
            httpServletResponse.setHeader("Authorization", prefix + jwt);
            return Result.OK(data).build();
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
    }

    @PostMapping(value = "/user/register")
    public Result registerUser(@RequestParam("telephone") Long telephone, @RequestParam("password") String password, @RequestParam("checkCode") int checkCode) {
        userService.userRegister(telephone, checkCode, passwordEncoder.encode(password));
        return Result.OK("注册成功").build();
    }

    @GetMapping("/user/info")
    public Result getUserInfo() {
        UserPrincipal currentUser = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        UserDO userDO = userService.getUserInfo(currentUser.getUserCode());
        return Result.OK(userDO).build();
    }

    @PutMapping("/user/password")
    public Result modifyUserPassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword, HttpServletResponse httpServletResponse) {
        UserPrincipal currentUser = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        userService.modifyUserPassword(currentUser.getUserCode(), oldPassword, newPassword);
        return Result.OK("更新用户信息成功").build();
    }

    @GetMapping("/user/account-opening-info")
    public Result getUserAccountOpeningInfo(@RequestParam("userCode") int userCode) {
        //UserPrincipal currentUser=jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        return Result.OK(userService.getAccountOpeningInfo(userCode)).build();
    }

    @PostMapping("/user/account-opening-info")
    public Result setUserAccountInfo(AcctOpenInfoDO acctOpenInfoDO, ImageDO imageDO) {
        UserPrincipal currentUser = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        //System.out.println(acctOpenInfoDO.toString()+imageDO.toString());
        userService.increaseAccountOpenInfo(currentUser.getUserCode(), acctOpenInfoDO, imageDO);
        return Result.OK("添加开户资料成功").build();
    }

    @GetMapping("/user/account-opening-info/status")
    public Result getUserAccountOpeningInfoStatus(@RequestParam("userCode") int userCode, HttpServletResponse response) {
        //response.setHeader("fuck","fuck");
        return Result.OK(userService.getUserAndState(userCode)).build();
    }

    @GetMapping("/user/check-code")
    public Result getCheckCode(@RequestParam("telephone") Long telephone) {
        userService.getOtp(telephone);
        return Result.OK("获取验证码成功").build();
    }

    @GetMapping("/bank")
    public Result getBankList() {
        return Result.OK(userService.getAllDataInfoList("BANK_TYPE", "acct_open_info")).build();
    }

    @GetMapping("/education")
    public Result getEducationList() {
        return Result.OK(userService.getAllDataInfoList("EDUCATION", "acct_open_info")).build();
    }

    @GetMapping("/user/audit/employee/time")
    public Result getUserListByEmployeeCodeAndStartTimeAndEndTIme(@RequestParam("pageNum") int pageNum,
                                                                  @RequestParam("employeeCode")String employeeCode,
                                                                  @RequestParam(value = "startTime",defaultValue = "null",required = false)Long startTime,
                                                                  @RequestParam(value = "endTime",defaultValue = "null",required = false)Long endTime){
        return Result.OK(userService.getUserListByEmployeeCodeAndStartTimeAndEndTIme(pageNum,employeeCode,startTime,endTime)).build();
    }
    //获取待审核用户列表
    @MethodLog(7)
    @GetMapping("/user/unaudited-users")
    public Result getUserListFiltered(@RequestParam("pageNum") int pageNum) {
        Map<String, Object> data = userService.getUserInfoList(pageNum, true);
        return Result.OK(data).build();
    }

    @GetMapping("/user/all-users")
    public Result getUserList(@RequestParam("pageNum") int pageNum) {
        Map<String, Object> data = userService.getUserInfoList(pageNum, false);
        return Result.OK(data).build();
    }


    @MethodLog(10)
    @GetMapping("/system-parameter")
    public Result getAllSystemParameter(@RequestParam("pageNum") int pageNum) {
        Map<String, Object> map = systemParameterService.getAllSystemParameters(pageNum);
        return Result.OK(map).build();
    }

    @MethodLog(11)
    @PutMapping("/system-parameter")
    public Result modifySystemParameter(@RequestParam("paraCode") int paraCode, @RequestParam("paraValue") String paraValue) {
        systemParameterService.modifySystemParameter(paraCode, paraValue);
        return Result.OK("修改参数成功").build();
    }


}
