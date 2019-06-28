package com.kht.backend.lifecycle;
import com.alibaba.fastjson.JSONObject;
import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dao.SysParaDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.dataobject.SysParaDO;
import com.kht.backend.service.DataDictionaryService;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.SystemParameterService;
import com.kht.backend.service.model.DataDictionaryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private RedisTemplate redisTemplate;
    @Resource
    private ValueOperations<String,Object> valueOperations;
    @Autowired
    private SystemParameterService systemParameterService;
    @Autowired
    private OrganizationDOMapper organizationDOMapper;
    @Autowired
    private DataDictionaryService dataDictionaryService;
    @Autowired
    private SysParaDOMapper sysParaDOMapper;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("通过实现ApplicationRunner接口，在spring boot项目启动后打印参数");
        String sysKey = "SystemPara";
        List<SysParaDO> allSystemParameters = sysParaDOMapper.listAll();
        valueOperations.set(sysKey,allSystemParameters);
        String orgKey = "OrganizationList";
        List<OrganizationDO> organizationDOList = organizationDOMapper.selectAll();
        //键值存储organization  key- org+主码，value-OrganizationDO
        for(int i = 0;i<organizationDOList.size();i++){
            String orgCode = organizationDOList.get(i).getOrgCode();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("org");
            stringBuffer.append(orgCode);
            orgCode = stringBuffer.toString();
            valueOperations.set(orgCode,organizationDOList.get(i));
        }
        //键值对存储organizationList key-OrganizationList value-List
        valueOperations.set(orgKey,organizationDOList);
        String datadictKey = "DataDictList";
        //键值对存储dataDictionary key-ColCode+TabCode+ValueCode  value-Value;
        List<DataDictionaryModel> allDataDictionaries = dataDictionaryService.getAllDataDictionaries();
        for(int i =0;i<allDataDictionaries.size();i++){
            StringBuffer datadictkey = new StringBuffer();
            datadictkey.append(allDataDictionaries.get(i).getColCode()).append(allDataDictionaries.get(i).getTabCode()).append(allDataDictionaries.get(i).getValueCode());
            String datadictkeyStr = datadictkey.toString();
            String value = allDataDictionaries.get(i).getValue();
            valueOperations.set(datadictkeyStr,value);
        }
        //键值对存储全部dictionary key-DataDictList  value-DataDictionaryList
        valueOperations.set(datadictKey,allDataDictionaries);
    }
}