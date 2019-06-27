package com.kht.backend.lifecycle;
import com.alibaba.fastjson.JSONObject;
import com.kht.backend.dao.OrganizationDOMapper;
import com.kht.backend.dao.SubDataDictDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("通过实现ApplicationRunner接口，在spring boot项目启动后打印参数");
        String sysKey = "SystemPara";
        List allSystemParameters = systemParameterService.getAllSystemParameters();
        valueOperations.set(sysKey,allSystemParameters);
        String orgKey = "OrganizationList";
        List<OrganizationDO> organizationDOList = organizationDOMapper.selectAll();
        valueOperations.set(orgKey,organizationDOList);
        String datadictKey = "DataDictList";
        List<DataDictionaryModel> allDataDictionaries = dataDictionaryService.getAllDataDictionaries();
        valueOperations.set(datadictKey,allDataDictionaries);
        //System.out.println(valueOperations.get(datadictKey));
        /*List<DataDictionaryModel> o = (List)valueOperations.get(datadictKey);
        for(int i =0;i<o.size();i++){
            if(o.get(i).getMainCode() == 19){
                String sktEx = o.get(i).getValueCode()
            }
        }*/
    }
}