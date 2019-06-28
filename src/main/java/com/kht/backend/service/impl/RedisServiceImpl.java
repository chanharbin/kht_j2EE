package com.kht.backend.service.impl;

import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.dataobject.SysParaDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class RedisServiceImpl {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SubDataDictDOMapper subDataDictDOMapper;
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Resource
    private ValueOperations<String, Object> valueOperations;

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
            
        }
        return null;
    }
}
