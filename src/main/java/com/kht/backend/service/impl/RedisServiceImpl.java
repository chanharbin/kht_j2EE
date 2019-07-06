package com.kht.backend.service.impl;

import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dao.PositionDOMapper;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dao.SysParaDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.dataobject.PositionDO;
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
    @Autowired
    private PositionDOMapper positionDOMapper;

    private final String jwtBlackKey = "jwtBlackKey";
    @Value("${app.jwtExpirationInMs}")
    private Long jwtExpirationInMs;

    /**
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
            valueOperations.set("org" + orgCode, organizationDO);
        }
        if (organizationDO != null) {
            return organizationDO.getOrgName();
        }
        return null;
    }

    public boolean updateOrganization(OrganizationDO organizationDO) {
        try {
            organizationDOMapper.updateByPrimaryKeySelective(organizationDO);
            String key = "org" + organizationDO.getOrgCode();
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 删除机构缓存
     *
     * @param orgCode
     * @return
     */
    public boolean deleteOrganization(String orgCode) {
        try {
            organizationDOMapper.deleteByPrimaryKey(orgCode);
            String key = "org" + orgCode;
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param paraName return 系统参数值
     */
    public String getParaValue(String paraName) {
        if (paraName == null) {
            return null;
        }
        String paraValue;
        String key = "sys" + paraName;
        SysParaDO sysParaDO = new SysParaDO();
        if (redisTemplate.hasKey(key)) {
            paraValue = (String) valueOperations.get(key);
        } else {
            sysParaDO = sysParaDOMapper.selectByParaName(paraName);
            valueOperations.set(key, sysParaDO.getParaValue());
            paraValue = sysParaDO.getParaValue();
        }
        return paraValue;
    }

    public boolean updataParaValue(SysParaDO sysParaDO) {
        String paraName = sysParaDO.getParaName();
        try {
            sysParaDOMapper.updateByPrimaryKey(sysParaDO);
            String key = "sys" + paraName;
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    /**
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
        } else {
            String value = subDataDictDOMapper.selectByColCodeAndTabCodeAndValueCode(colCode, tabCode, valueCode);
            valueOperations.set(key, value);
            return value;
        }
    }

    /**
     *更新数据字典
     */
    /*public boolean updateDataDic( ){

    }*/

    /**
     * @return 系统参数列表
     */
    public List<SysParaDO> getSystemParameterList() {
        String sysKey = "SystemPara";
        if (redisTemplate.hasKey(sysKey)) {
            System.out.println("get from redis");
            return (List<SysParaDO>) valueOperations.get(sysKey);
        } else {
            List<SysParaDO> sysParaDOS = sysParaDOMapper.listAll();
            valueOperations.set(sysKey, sysParaDOS);
            return sysParaDOS;
        }
    }

    public boolean updataSysParaList() {
        try {
            String key = "SystemPara";
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param posCode return posName
     */
    public String getPosName(int posCode) {
        StringBuffer stringBuffer = new StringBuffer("position");
        stringBuffer.append(posCode);
        String key = stringBuffer.toString();
        if (redisTemplate.hasKey(key)) {
            System.out.println("get from redis");
            return (String) valueOperations.get(key);
        } else {
            PositionDO positionDO = positionDOMapper.selectByPrimaryKey(posCode);
            valueOperations.set(key, positionDO.getPosName());
            return positionDO.getPosName();
        }
    }

    public boolean updataPosName(PositionDO positionDO) {
        try {
            Integer posCode = positionDO.getPosCode();
            String key = "position" + String.valueOf(posCode);
            System.out.println(key);
            if (redisTemplate.hasKey(key)) {
                redisTemplate.delete(key);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 存放最新token的产生时间
     *
     * @param userCode
     */
    public void setJwtBlackList(int userCode, long time) {
        valueOperations.set(jwtBlackKey + userCode, time, jwtExpirationInMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取最新token的产生时间
     *
     * @param userCode
     * @return
     */
    public long getJwtTime(int userCode) {
        if (redisTemplate.hasKey(jwtBlackKey + userCode)) {
            return (long) valueOperations.get(jwtBlackKey + userCode);
        }
        return 0L;
    }

    /*public boolean getJwtKeyStatus(int useCode){
        if(redisTemplate.hasKey())
    }*/
}
