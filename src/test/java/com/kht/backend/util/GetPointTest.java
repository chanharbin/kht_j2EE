package com.kht.backend.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class GetPointTest {

    @Test
    public void getPoint() {
        char[] ans = new char[10];
        for(int i =0;i<10;i++){
            ans[i] = 'E';
        }
        GetPoint getPoint = new GetPoint(ans);
        int point = getPoint.getPoint();
        System.out.println(point);
        String investorType = getPoint.getInvestorType();
        System.out.println(investorType);
    }
}