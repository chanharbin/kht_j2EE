package com.kht.backend.util;

import org.springframework.stereotype.Component;

@Component
public class IdProvider {

    private long twepoch=1560667623144L;

    private final long sequenceBits=2L;

    private final long timestampLeftShift=sequenceBits;

    private final long sequenceMask=-1L^(-1L<<sequenceBits);

    private long sequence=0L;

    private long lastTimestamp=-1L;

    private final long charBits=5;

    private final long charMasks=-1L^(-1L<<charBits);

    private final char[] bitTochar={'0','1','2','3','4', '5','6','7','8','9', 'A','B','C','D','E', 'F','G','H','J','K', 'L','M','N','P','Q', 'R','S','T','U','V', 'W','S'};

    public IdProvider(){}

    /**
     *
     * @param prefix
     * @return 生成不重复的id
     */
    public String getId(String prefix) {
        long suffix=nextId();
        char[] tempString=new char[8];
        for(int i=7;i>=0;i--) {
            tempString[i]=bitTochar[(int)(suffix&charMasks)];
            suffix>>=charBits;
        }
        return prefix+new String(tempString);
    }
    /**
     * 线程安全
     * @return 返回不重复的id
     */

    protected synchronized long nextId() {
        long timestamp=timeGen();
        if(timestamp<lastTimestamp) {
            throw new RuntimeException(
                    String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }
        if(timestamp==lastTimestamp) {
            sequence=(sequence+1)&sequenceMask;
            if(sequence==0){
                timestamp=tilNextMillis(timestamp);
            }
        }
        else{
            sequence=0L;
        }
        lastTimestamp=timestamp;
        System.out.println(sequence);
        System.out.println(timestamp);
        return ((timestamp-twepoch))<<timestampLeftShift|sequence;
    }

    /**
     * 延迟1ms
     * @param lastTimestamp
     * @return 延迟后的时间戳
     */
    protected long tilNextMillis(long lastTimestamp) {
        //System.out.println("lastTimestamp: "+lastTimestamp);
        long timestamp=timeGen();
        while(timestamp<=lastTimestamp) {
            timestamp=timeGen();
        }
        return  timestamp;
    }
    /**
     *
     * @return 当前时间戳
     */
    protected long timeGen() {
        return System.currentTimeMillis();
    }
}
