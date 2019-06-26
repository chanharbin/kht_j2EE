package com.kht.backend.util;

public class GetPoint {
    private int[] ans = new int[10];
    private int result;
    public GetPoint(char[] ans1){
        for(int i =0;i<10;i++){
            ans[i] = (ans1[i] - 'A' + 1)*2;
        }
    }
    public int getPoint(){
        for(int i = 0;i<10;i++){
            result += ans[i];
        }
        return result;
    }
    public String getInvestorType(){
        int point = getPoint();
        if(point>=0&&point<=50){
            return "0";
        }
        if(point>50&&point<80){
            return "1";
        }
        else
            return "2";
    }
}
