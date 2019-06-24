package com.kht.backend.controller;

import com.kht.backend.entity.Result;
import com.kht.backend.exception.AuthenticationException;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/login")
    public Result authenticateUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password){
        System.out.println("fuck");
        try {
            Authentication authentication =authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(telephone, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt=jwtTokenProvider.generateToken(authentication);
            System.out.println(jwt);
            return Result.OK(jwt).build();
        } catch (DisabledException e) {
            throw new AuthenticationException("User is disabled!", e);
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Bad credentials!", e);
        }
        //Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("123456789","123456"));
                //new UsernamePasswordAuthenticationToken(telephone, password));

    }
    @PostMapping("/register")
    public Result registerUser(@RequestParam("telephone") Long telephone,@RequestParam("password")String password,@RequestParam("check_code")String checkCode){
        return userService.userRegister(telephone,"0",passwordEncoder.encode(password));
       // return userService.userRegister(123456789L,"0",passwordEncoder.encode("123456"));
    }
}
