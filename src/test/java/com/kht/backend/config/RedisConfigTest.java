package com.kht.backend.config;

import com.alibaba.fastjson.JSON;
import com.kht.backend.dataobject.PositionDO;
import com.kht.backend.service.RedisService;
import com.kht.backend.util.RedisKeyUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @Resource
    private ValueOperations<String,Object> valueOperations;

    @Autowired
    private HashOperations<String, String, Object> hashOperations;

    @Autowired
    private ListOperations<String, Object> listOperations;

    @Autowired
    private SetOperations<String, Object> setOperations;

    @Autowired
    private ZSetOperations<String, Object> zSetOperations;

    @Resource
    private RedisService redisService;

    @Test
    public void testObj() throws Exception{
        PositionDO positionDO = new PositionDO();
        positionDO.setPosCode(1);
        positionDO.setPosName("123");
        ValueOperations<String,Object> operations = redisTemplate.opsForValue();
        redisService.expireKey("name",20, TimeUnit.SECONDS);
        String key = RedisKeyUtil.getKey(PositionDO.Table,"name",positionDO.getPosName());
        PositionDO po = (PositionDO) operations.get(key);
        System.out.println(po);
    }

    @Test
    public void testValueOption( )throws  Exception{
        PositionDO positionDO = new PositionDO();
        positionDO.setPosCode(1);
        positionDO.setPosName("123");
        PositionDO positionDO1 = new PositionDO();
        positionDO1.setPosCode(1);
        positionDO1.setPosName("123");
        List polist = new LinkedList();
        polist.add(positionDO);
        polist.add(positionDO1);
        valueOperations.set("test",polist);
        String s = JSON.toJSONString(valueOperations.get("test"));
        System.out.println(s);
    }

    @Test
    public void testSetOperation() throws Exception{
        PositionDO positionDO = new PositionDO();
        positionDO.setPosName("123");
        positionDO.setPosCode(1);
        PositionDO poDO = new PositionDO();
        poDO.setPosCode(2);
        poDO.setPosName("321");
        setOperations.add("user:test",positionDO,poDO);
        Set<Object> result = setOperations.members("user:test");
        String s = JSON.toJSONString(result);
        System.out.println(s);
    }

    @Test
    public void HashOperations() throws Exception{
        PositionDO positionDO = new PositionDO();
        positionDO.setPosCode(1);
        positionDO.setPosName("123");
        hashOperations.put("hash:user",positionDO.hashCode()+"",positionDO);
        System.out.println(hashOperations.get("hash:user",positionDO.hashCode()+""));
    }

    @Test
    public void  ListOperations() throws Exception{
        PositionDO positionDO = new PositionDO();
        positionDO.setPosCode(1);
        positionDO.setPosName("123");
        listOperations.leftPush("list:user",positionDO);
        PositionDO positionDO1 = new PositionDO();
        positionDO1.setPosCode(2);
        positionDO1.setPosName("321");
        listOperations.leftPush("list:user",positionDO);
        System.out.println(listOperations.leftPop("list:user"));
        // pop之后 值会消失
        System.out.println(listOperations.leftPop("list:user"));
    }
}