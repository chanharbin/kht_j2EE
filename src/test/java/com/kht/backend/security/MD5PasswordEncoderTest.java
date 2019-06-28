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
        System.out.println(md5PasswordEncoder.matches("1","c4ca4238a0b923820dcc509a6f75849b"));
    }
}