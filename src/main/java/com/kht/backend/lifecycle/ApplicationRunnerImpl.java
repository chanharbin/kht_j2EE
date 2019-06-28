package com.kht.backend.lifecycle;
import com.alibaba.fastjson.JSONObject;
import com.kht.backend.dao.*;
import com.kht.backend.dataobject.OperationDO;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.dataobject.PositionDO;
import com.kht.backend.dataobject.SysParaDO;
import com.kht.backend.service.DataDictionaryService;
import com.kht.backend.service.OperationLogService;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.SystemParameterService;
import com.kht.backend.service.model.DataDictionaryModel;
import com.kht.backend.service.model.UserGrantedAuthority;
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
    @Autowired
    private OperationDOMapper operationDOMapper;
    @Autowired
    private PositionDOMapper positionDOMapper;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("通过实现ApplicationRunner接口，在spring boot项目启动后打印参数");
        String sysKey = "SystemPara";
        List<SysParaDO> allSystemParameters = sysParaDOMapper.listAll();
        //键值对存储系统参数 key- sys+PARANAME  value-PARAVALUE
        for(int i =0;i<allSystemParameters.size();i++){
            String paraName = allSystemParameters.get(i).getParaName();
            StringBuffer stringBuffer = new StringBuffer("sys");
            stringBuffer.append(paraName);
            paraName = stringBuffer.toString();
            valueOperations.set(paraName,allSystemParameters.get(i).getParaValue());
        }
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

        //键值对存储岗位表
        List<PositionDO> positionDOList = positionDOMapper.listAll();
        for(int i = 0;i<positionDOList.size();i++){
            String posKey;
            StringBuffer stringBuffer = new StringBuffer("position");
            stringBuffer.append(positionDOList.get(i).getPosCode());
            posKey = stringBuffer.toString();
            valueOperations.set(posKey,positionDOList.get(i).getPosName());
        }

        //键值对存储操作表
        List<OperationDO> operationDOList = operationDOMapper.listAll();
        for(int i = 0;i<operationDOList.size();i++){
            StringBuffer stringBuffer = new StringBuffer("operation");
            stringBuffer.append(operationDOList.get(i).getOperaName());
            String operaKey = stringBuffer.toString();
            valueOperations.set(operaKey,operationDOList.get(i));
        }
        /*System.out.println(JSONObject.toJSONString(valueOperations.get("operation员工登录")));
        System.out.println(valueOperations.get("position1"));*/
    }
}