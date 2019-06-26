package com.kht.backend.service;

import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;

import java.util.List;

public interface SystemParameterService {

    public List getAllSystemParameters();

    public Result modifySystemParameter(int paraCode, String paraValue);
}
