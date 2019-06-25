package com.kht.backend.controller;

import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.exception.AuthenticationException;
import com.kht.backend.security.CurrentUser;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import com.kht.backend.service.impl.UserServiceImpl;
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
    private HttpServletRequest httpServletRequest;

    private final String prefix="Bearer ";
    @PostMapping(value = {"/user/login","/employee/login"})
    public Result authenticateUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password){
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(telephone, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt=jwtTokenProvider.generateToken(authentication);
            return Result.OK(prefix+jwt).build();
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }

    }
    @PostMapping("/register")
    public Result registerUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password,@RequestParam("checkCode")String checkCode){
        if(userService.userRegister(telephone,checkCode,passwordEncoder.encode(password)))
            return Result.OK("注册成功").build();
        else
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"注册失败");
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
        return userService.modifyUserInfo(currentUser.getUserCode(),passwordEncoder.encode(newPassword));
    }
}
