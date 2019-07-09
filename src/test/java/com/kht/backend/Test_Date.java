/*
package com.kht.backend;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class Test_Date {
    @Test
    public void getDate(){
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
        String date = smf.format(new Date());
        String replace = date.replace("-", "");
        System.out.println(Long.parseLong(replace));
    }

    @Test
    public void getOtp(){
        //需要按照一定的规则生成OTP验证码
        Random random = new Random();
        int randomInt = random.nextInt(8999);
        randomInt+=1000;
        String optCode = String.valueOf(randomInt);
        System.out.println(optCode);
    }

}
*/
