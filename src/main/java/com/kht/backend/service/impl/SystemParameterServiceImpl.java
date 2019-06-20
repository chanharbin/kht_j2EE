package com.kht.backend.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.kht.backend.dao.SysparaDOMapper;
import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.dataobject.SysparaDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.SystemParameterService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SystemParameterServiceImpl implements SystemParameterService {
    @Autowired
    SysparaDOMapper sysparaDOMapper;
    @Override
    public Result getAllSystemParameters(int pageNum) {
        PageHelper.startPage(pageNum,10);
        List<SysparaDO> sysparaDOList=sysparaDOMapper.listAll();
        if(sysparaDOList==null){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"获取参数失败");
        }
        PageInfo<SysparaDO> page = new PageInfo<>(sysparaDOList);
        Map<String,Object> resultData = new LinkedHashMap<>();
        resultData.put("totalNum",page.getTotal());
        resultData.put("data",page.getList());
        return null;
    }

    @Override
    public Result modifySystemParameter(int paraCode, String paraValue) {
        int affectRow=sysparaDOMapper.updateParaValueByPrimaryKeySelective(paraCode,paraValue);
        if(affectRow==0){
            throw new ServiceException(ErrorCode.SERVER_EXCEPTION,"修改参数失败");
        }
        return Result.OK("修改参数成功").build();
    }
}
