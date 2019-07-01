package com.kht.backend;


import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Test_Date {
    @Test
    public void getDate(){
        SimpleDateFormat smf = new SimpleDateFormat("yyyy-MM-dd");
        String date = smf.format(new Date());
        String replace = date.replace("-", "");
        System.out.println(Long.parseLong(replace));
    }

}
