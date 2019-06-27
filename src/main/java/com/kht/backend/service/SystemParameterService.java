package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

import java.util.List;
import java.util.Map;

public interface SystemParameterService {

    public Map<String,Object> getAllSystemParameters(int pageNum);

    public void modifySystemParameter(int paraCode, String paraValue);
}
