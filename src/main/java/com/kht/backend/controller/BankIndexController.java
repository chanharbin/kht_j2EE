package com.kht.backend.controller;

import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dao.UserDOMapper;
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
    @Autowired
    private UserDOMapper userDOMapper;
    @GetMapping("/manage/index")
    public Result getIndex(){
        /*int allCount = acctOpenInfoDOMapper.getAllCount();*/
        //总用户数
        int userNum = userDOMapper.selectUserCount();
        List<AcctOpenInfoDO> acctOpenInfoDOS = acctOpenInfoDOMapper.listAll();
        //总提交资料数
        int allCount = acctOpenInfoDOS.size();
        //未提交资料用户数
        int nonApply = userNum - allCount;
        //审核不通过的用户数
        int noSuccessNum = 0;
        //通过审核的用户数
        int validatedUser = 0;
        Map map = new HashMap<String,Integer>();
        for(int i =0;i<acctOpenInfoDOS.size();i++){
            AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOS.get(i);
            if(acctOpenInfoDO.getInfoStatus().equals("1")){
                validatedUser++;
            }
            if(acctOpenInfoDO.getInfoStatus().equals("2")){
                noSuccessNum++;
            }
        }
        int nonValidatedUser = allCount - validatedUser;
        map.put("已审核用户",validatedUser);
        map.put("未审核用户",nonValidatedUser);
        List<EmployeeDO> employeeDOList = employeeDOMapper.listAll();
        int employeeNum = employeeDOList.size();
        map.put("员工数",employeeNum);
        map.put("待提交审核资料用户",nonApply);
        map.put("未通过审核用户",noSuccessNum);
        return Result.OK(map).build();
    }
}
