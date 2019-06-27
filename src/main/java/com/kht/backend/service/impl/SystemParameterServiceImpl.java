
package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.SysParaDOMapper;
import com.kht.backend.dataobject.OrganizationDO;
import com.kht.backend.dataobject.SysParaDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.SystemParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
@Service
public class SystemParameterServiceImpl implements SystemParameterService {
    @Autowired
    SysParaDOMapper sysparaDOMapper;
    @Value("${app.pageSize}")
    private int pageSize;
    @Override
    public Map<String,Object>  getAllSystemParameters(int pageNum){
        PageHelper.startPage(pageNum,pageSize);
        List<SysParaDO> sysParaDOList =sysparaDOMapper.listAll();
        if(sysParaDOList==null||sysParaDOList.isEmpty()){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"无系统参数");
        }
        PageInfo<SysParaDO> page = new PageInfo<>(sysParaDOList);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("parameterNum",page.getTotal());
        resultData.put("parameters",page.getList());
        return resultData;
    }

    @Override
    public void modifySystemParameter(int paraCode, String paraValue) {
        int affectRow=sysparaDOMapper.updateParaValueByPrimaryKeySelective(paraCode,paraValue);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改参数失败");
        }
        //return Result.OK("修改参数成功").build();
    }
}
