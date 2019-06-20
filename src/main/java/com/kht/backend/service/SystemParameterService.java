package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

public interface SystemParameterService {

    public Result getAllSystemParameters(int pageNum);

    public Result modifySystemParameter(int paraCode, String paraValue);
}
