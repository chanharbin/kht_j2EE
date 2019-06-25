package com.kht.backend.controller;

import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.Result;
import com.kht.backend.service.EmployeeService;
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
    //新增员工
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
    @RequestMapping(value = "/employee",method = DELETE)
    public Result deleteEmployee(@RequestParam("EMPLOYEE_CODE")String employeeCode){
        return employeeService.deleteEmployee(employeeCode);
    }
    //修改员工信息
    @RequestMapping(value = "/employee",method = PUT)
    public Result modifyEmployee(@RequestParam("POS_CODE") int posCode,
                                 @RequestParam("EMPLOYEE_NAME")String employeeName,
                                 @RequestParam("ID_CODE")String idCode,
                                 @RequestParam("EMAIL")String email,
                                 @RequestParam("ADDRESS")String address,
                                 @RequestParam("EMPLOYEE_PWD")String pwd,
                                 @RequestParam("TELPHONE")long telphone){
        UserPrincipal userPrincipalFromRequest = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        EmployeeDO employeeDO = new EmployeeDO();
        UserDO userDO = new UserDO();
        employeeDO.setEmployeeCode(userPrincipalFromRequest.getCode());
        employeeDO.setUserCode(userPrincipalFromRequest.getUserCode());
        employeeDO.setTelephone(telphone);
        employeeDO.setAddress(address);
        employeeDO.setEmail(email);
        employeeDO.setIdCode(idCode);
        employeeDO.setPosCode(posCode);
        employeeDO.setEmployeeName(employeeName);
        pwd = passwordEncoder.encode(pwd);
        System.out.println(pwd);
        userDO.setPassword(pwd);
        userDO.setTelephone(telphone);
        userDO.setUserCode(userPrincipalFromRequest.getUserCode());
        userDO.setUserType("1");
        Result result = employeeService.modifyEmployee(employeeDO, userDO);
        return result;
    }
    //根据姓名获取员工
    @GetMapping(value = "/employee/name")
    public Result getEmployeeByName(@RequestParam("name")String name){
        return employeeService.getEmployeeByName(name);
    }
    //获取员工列表
    @GetMapping(value = "/employee")
    public Result getEmployeeList(@RequestParam("page_num")int pageNum){
        Result result = employeeService.listEmployee(pageNum);
        return result;
    }

}
