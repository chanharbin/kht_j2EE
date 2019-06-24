package com.kht.backend.security;

import org.junit.Test;

import static org.junit.Assert.*;

public class MD5PasswordEncoderTest {
    MD5PasswordEncoder md5PasswordEncoder=new MD5PasswordEncoder();
    @Test
    public void encode() {
        String tmp=md5PasswordEncoder.encode("123456");
        System.out.println(tmp);
    }

    @Test
    public void matches() {
        System.out.println(md5PasswordEncoder.matches("123456","a448410bdcbb4d7cfb32830909f6aa08"));
    }
}