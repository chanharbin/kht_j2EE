package com.kht.backend.controller;

import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@RestController
public class BankIndexController {
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;
    @Autowired
    private EmployeeDOMapper employeeDOMapper;
    @GetMapping("/manage/index")
    public Result getIndex(){
        /*int allCount = acctOpenInfoDOMapper.getAllCount();*/
        List<AcctOpenInfoDO> acctOpenInfoDOS = acctOpenInfoDOMapper.listAll();
        int allCount = acctOpenInfoDOS.size();
        int validatedUser = 0;
        Map map = new HashMap<String,Integer>();
        for(int i =0;i<acctOpenInfoDOS.size();i++){
            AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOS.get(i);
            if(acctOpenInfoDO.getInfoStatus().equals("1")){
                validatedUser++;
            }
        }
        int nonValidatedUser = allCount - validatedUser;
        map.put("已审核用户",validatedUser);
        map.put("未审核用户",nonValidatedUser);
        List<EmployeeDO> employeeDOList = employeeDOMapper.listAll();
        int employeeNum = employeeDOList.size();
        map.put("员工数",employeeNum);
        return Result.OK(map).build();
    }

}
