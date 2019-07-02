package com.kht.backend.controller;


import com.kht.backend.aspect.MethodLog;
import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.EmployeeService;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmployeeDOMapper employeeDOMapper;
    //新增员工
    @Autowired
    private AcctOpenInfoDOMapper acctOpenInfoDOMapper;
    @Autowired
    private OrganizationService organizationService;
    @MethodLog(4)
    @RequestMapping(value = "/employee", method = POST)
    public Result increaseEmployee(@RequestParam("EMPLOYEE_NAME")String employeeName,
                                   @RequestParam("ID_CODE")String idCode,
                                   @RequestParam("TELEPHONE")Long telphone,
                                   @RequestParam("EMAIL") String mail,
                                   @RequestParam("ADDRESS") String address,
                                   @RequestParam("POS_CODE")int posCode,
                                   @RequestParam("EMPLOYEE_STATUS") String employeeStatus,
                                   @RequestParam("ORG_CODE")String orgCode,
                                   @RequestParam("EMPLOYEE_PWD")String pwd){
        EmployeeDO employeeDO = new EmployeeDO();
        UserDO userDO = new UserDO();
        employeeDO.setTelephone(telphone);
        employeeDO.setEmployeeName(employeeName);
        employeeDO.setPosCode(posCode);
        employeeDO.setIdCode(idCode);
        employeeDO.setEmail(mail);
        employeeDO.setAddress(address);
        employeeDO.setEmployeeStatus(employeeStatus);
        userDO.setUserType("1");
        userDO.setTelephone(telphone);
        pwd = passwordEncoder.encode(pwd);
        userDO.setPassword(pwd);
        Result result = employeeService.increaseEmployee(employeeDO, orgCode,userDO);
        return result;
    }
    //删除员工
    @MethodLog(6)
    @RequestMapping(value = "/employee",method = DELETE)
    public Result deleteEmployee(@RequestParam("EMPLOYEE_CODE")String employeeCode){
        return employeeService.deleteEmployee(employeeCode);
    }
    //ToTest
    //修改员工信息
    @MethodLog(5)
    @RequestMapping(value = "/employee",method = PUT)
    public Result modifyEmployee(@RequestParam("employeeCode") String employeeCode,
                                 @RequestParam("userCode")int userCode,
                                 @RequestParam("posCode") int posCode,
                                 @RequestParam("employeeName")String employeeName,
                                 @RequestParam("idCode")String idCode,
                                 @RequestParam("email")String email,
                                 @RequestParam("address")String address,
                                 @RequestParam("employeePwd")String pwd,
                                 @RequestParam("telephone")long telphone,
                                 @RequestParam("employeeStatus")String employeeStatus){
        /*UserPrincipal userPrincipalFromRequest = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        int userCode_I = userPrincipalFromRequest.getUserCode();
        EmployeeDO employeeDOI = employeeDOMapper.selectByUserCode(userCode_I);
        Integer i_posCode = employeeDOI.getPosCode();
        if(i_posCode <= 3 || i_posCode <= posCode){
            return Result.OK("您当前无权限修改信息").build();
        }
        else{*/
        EmployeeDO employeeDO = new EmployeeDO();
        UserDO userDO = new UserDO();
        employeeDO.setUserCode(userCode);
        employeeDO.setEmployeeCode(employeeCode);
        employeeDO.setTelephone(telphone);
        employeeDO.setAddress(address);
        employeeDO.setEmail(email);
        employeeDO.setIdCode(idCode);
        if(employeeStatus.equals("1")||employeeStatus.equals("2")){
            employeeDO.setPosCode(1);
        }
        employeeDO.setPosCode(posCode);
        employeeDO.setEmployeeName(employeeName);
        employeeDO.setEmployeeStatus(employeeStatus);
        pwd = passwordEncoder.encode(pwd);
        userDO.setPassword(pwd);
        userDO.setTelephone(telphone);
        userDO.setUserCode(userCode);
        userDO.setUserType("1");
        Result result = employeeService.modifyEmployee(employeeDO, userDO);
        return result;
    }
    //根据姓名获取员工
    @MethodLog(3)
    @GetMapping(value = "/employee/name")
    public Result getEmployeeByName(@RequestParam("name")String name){
        return employeeService.getEmployeeByName(name);
    }
    //获取员工列表
    @MethodLog(2)
    @GetMapping(value = "/employee")
    public Result getEmployeeList(@RequestParam("page_num")int pageNum){
        Result result = employeeService.listEmployee(pageNum);
        return result;
    }

    //用户审核
    @MethodLog(10)
    @RequestMapping(value = "/user/audit",method = PUT)
    public Result validateUser(@RequestParam("INFO_CODE")int infoCode,
                               @RequestParam("INFO_STATUS")String infoStatus){
        if(infoStatus.equals("success")){
            employeeService.getUserValidationInfo(infoCode);
            return Result.OK("审核通过结果结果已提交").build();
        }
        else{
            AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByInfoCode(infoCode);
            acctOpenInfoDO.setInfoStatus("2");
            acctOpenInfoDOMapper.updateByPrimaryKey(acctOpenInfoDO);
            return Result.OK("审核未通过结果已提交").build();
        }
    }

    //根据机构名获取用户列表
    @MethodLog(9)
    @RequestMapping(value = "/user/organization",method = GET)
    public Result getUserListByOrgCode(@RequestParam("page_num")int pageNum,
                                       @RequestParam("orgCode") String orgCode){
        System.out.println(orgCode);
        System.out.println(pageNum);
        Result organizationUser = organizationService.getOrganizationUser(orgCode, pageNum);
        return organizationUser;
    }



}
