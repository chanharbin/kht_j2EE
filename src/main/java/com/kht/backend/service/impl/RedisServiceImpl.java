package com.kht.backend.service.impl;

import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dao.SysParaDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.dataobject.SysParaDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SubDataDictDOMapper subDataDictDOMapper;
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private SysParaDOMapper sysParaDOMapper;
    @Resource
    private ValueOperations<String, Object> valueOperations;

    private final String jwtBlackKey="jwtBlackKey";
    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;
    /**
     *
     * @param orgCode
     * @return 机构名称
     */
    public String getOrganizationName(String orgCode) {
        if (orgCode == null) {
            return null;
        }
        OrganizationDO organizationDO;
        if (redisTemplate.hasKey("org" + orgCode)) {
            organizationDO = (OrganizationDO) valueOperations.get("org" + orgCode);
        } else {
            organizationDO = organizationDOMapper.selectByPrimaryKey(orgCode);
        }
        if (organizationDO != null) {
            return organizationDO.getOrgName();
        }
        return null;
    }
    public boolean updateOrg;

    /**
     *
     * @param colCode
     * @param tabCode
     * @param valueCode
     * @return 数据字典对应值
     */
    public String getDataDictionary(String colCode, String tabCode, String valueCode) {
        if (colCode == null || tabCode == null || valueCode == null) {
            return null;
        }
        String key = colCode + tabCode + valueCode;
        if (redisTemplate.hasKey(key)) {
            return (String) valueOperations.get(key);
        }
        return subDataDictDOMapper.selectByColCodeAndTabCodeAndValueCode(colCode, tabCode, valueCode);
    }

    /**
     * @return 系统参数列表
     */
    public List<SysParaDO> getSystemParameterList(){
        String sysKey = "SystemPara";
        if(redisTemplate.hasKey(sysKey)){
            System.out.println("get from redis");
            return (List<SysParaDO>)valueOperations.get(sysKey);
        }
        else{
            return sysParaDOMapper.listAll();
        }
    }

    /**
     * 生成token的黑名单
     * @param userCode
     */
    public void setJwtBlackList(int userCode,Date time){
        valueOperations.set(jwtBlackKey+userCode,time,jwtExpirationInMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取最新token的产生时间
     * @param userCode
     * @return
     */
    public Date getJwtTime(int userCode){
        if(redisTemplate.hasKey(jwtBlackKey+userCode)){
            return (Date)valueOperations.get(jwtBlackKey+userCode);
        }
        return new Date(0L);
    }
}
