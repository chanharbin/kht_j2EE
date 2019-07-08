package com.kht.backend.controller;


import com.kht.backend.aspect.MethodLog;
import com.kht.backend.dao.AcctOpenInfoDOMapper;
import com.kht.backend.dao.EmployeeDOMapper;
import com.kht.backend.dao.UserDOMapper;
import com.kht.backend.dataobject.AcctOpenInfoDO;
import com.kht.backend.dataobject.EmployeeDO;
import com.kht.backend.dataobject.UserDO;
import com.kht.backend.entity.ErrorCode;
import com.kht.backend.entity.Result;
import com.kht.backend.entity.ServiceException;
import com.kht.backend.service.EmployeeService;
import com.kht.backend.service.OrganizationService;
import com.kht.backend.service.impl.RedisServiceImpl;
import com.kht.backend.service.model.UserPrincipal;
import com.kht.backend.util.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class EmployeeController {
    @Autowired
    private UserDOMapper userDOMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
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
    @Autowired
    private RedisServiceImpl redisService;
    private final String prefix = "Bearer ";
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
        UserPrincipal userPrincipalFromRequest = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        int userCode_I = userPrincipalFromRequest.getUserCode();
        EmployeeDO employeeDOI = employeeDOMapper.selectByUserCode(userCode_I);
        Integer i_posCode = employeeDOI.getPosCode();
        EmployeeDO employeeDO = new EmployeeDO();
        UserDO userDO = new UserDO();
        UserDO userDO1 = userDOMapper.selectByPrimaryKey(userCode);
        employeeDO.setUserCode(userCode);
        employeeDO.setEmployeeCode(employeeCode);
        employeeDO.setTelephone(telphone);
        employeeDO.setAddress(address);
        employeeDO.setEmail(email);
        employeeDO.setIdCode(idCode);
        if(employeeStatus.equals("1")||employeeStatus.equals("2")){
            employeeDO.setPosCode(1);
        }
        if(i_posCode >= 3) {
            if(i_posCode >= posCode){
                employeeDO.setPosCode(posCode);
            }
            else{
                throw new ServiceException(ErrorCode.FORBIDDEN__EXCEPTION,"您无法更改比您更高级的员工的岗位信息。");
            }
        }
        else{
            throw new ServiceException(ErrorCode.FORBIDDEN__EXCEPTION,"您无权限修改其他员工信息");
        }
        employeeDO.setEmployeeName(employeeName);
        employeeDO.setEmployeeStatus(employeeStatus);
        if(!pwd.equals("1")) {
            pwd = passwordEncoder.encode(pwd);
            userDO.setPassword(pwd);
        }
        else{
            userDO.setPassword(userDO1.getPassword());
        }
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

    @Transactional
    //用户审核
    @MethodLog(10)
    @RequestMapping(value = "/user/audit",method = PUT)
    public Result validateUser(@RequestParam("INFO_CODE")int infoCode,
                               @RequestParam("INFO_STATUS")String infoStatus,
                               @RequestParam("FAIL_MSG")String msg){
        if(infoStatus.equals("success")){
            AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByInfoCode(infoCode);
            redisService.setRefreshStatus(acctOpenInfoDO.getUserCode(), true);
            employeeService.getUserValidationInfo(infoCode,msg);
            return Result.OK("审核通过结果结果已提交").build();
        }
        else{
            AcctOpenInfoDO acctOpenInfoDO = acctOpenInfoDOMapper.selectByInfoCode(infoCode);
            if(acctOpenInfoDO.getInfoStatus().equals("2") || acctOpenInfoDO.getInfoStatus().equals("1")){
                throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"该用户已被审核，请勿重复提交");
            }
            else {
                acctOpenInfoDO.setInfoStatus("2");
                UserPrincipal currentUser = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
                acctOpenInfoDO.setEmployeeCode(currentUser.getCode());
                acctOpenInfoDO.setAuditRemark(msg);
                acctOpenInfoDOMapper.updateByPrimaryKey(acctOpenInfoDO);
                redisService.setRefreshStatus(acctOpenInfoDO.getUserCode(), true);
                return Result.OK("审核未通过结果已提交").build();
            }
        }
    }

    //根据机构名获取已审核用户列表
    @MethodLog(9)
    @RequestMapping(value = "/user/organization",method = GET)
    public Result getUserListByOrgCode(@RequestParam("page_num")int pageNum,
                                       @RequestParam("orgCode") String orgCode){
        UserPrincipal userPrincipalFromRequest = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        if(orgCode.equals(userPrincipalFromRequest.getCode().substring(0,4)) || userPrincipalFromRequest.getCode().substring(0,4).equals("0000")) {
            Result organizationUser = organizationService.getOrganizationUser(orgCode, pageNum,true);
            return organizationUser;
        }
        else{
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"您无法查询其他营业厅的用户列表");
        }
    }
    //根据机构名获取未审核用户列表
    @RequestMapping(value = "/inaudit_user/organization",method = GET)
    public Result getInAuditUserListByOrgCode(@RequestParam("page_num")int pageNum,
                                       @RequestParam("orgCode") String orgCode){
        UserPrincipal userPrincipalFromRequest = jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest);
        if(orgCode.equals(userPrincipalFromRequest.getCode().substring(0,4)) || userPrincipalFromRequest.getCode().substring(0,4).equals("0000")) {
            Result organizationUser = organizationService.getOrganizationUser(orgCode, pageNum,false);
            return organizationUser;
        }
        else{
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"您无法查询其他营业厅的用户列表");
        }
    }

    //员工登录
    @PostMapping("/employee/login")
    public Result authenticateUser(@RequestParam("telephone") Long telephone, @RequestParam("password") String password, HttpServletResponse httpServletResponse) {
        EmployeeDO employeeDO1 = employeeDOMapper.selectByTelephone(telephone);
        if(employeeDO1 == null){
            throw new ServiceException(ErrorCode.PARAM_ERR_COMMON,"暂无此账号");
        }
        if (employeeDO1.getEmployeeStatus().equals("0")) {
            try {
                Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(String.valueOf(telephone), password));
                UserPrincipal userPrincipal = ( UserPrincipal )authentication.getPrincipal();
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String jwt = jwtTokenProvider.generateToken(authentication);
                Map<String, Object> data = new LinkedHashMap<>();
                if (userPrincipal.getUserType().equals("1")) {
                    EmployeeDO employeeDO = employeeDOMapper.selectByPrimaryKey(userPrincipal.getCode());
                    String employeeName = employeeDO.getEmployeeName();
                    String position = redisService.getPosName(employeeDO.getPosCode());
                    data.put("posCode", employeeDO.getPosCode());
                    data.put("employeeName", employeeName);
                    data.put("employeePosition", position);
                    data.put("orgCode", userPrincipal.getCode().substring(0, 4));
                }
                httpServletResponse.setHeader("jwtauthorization", prefix + jwt);
                return Result.OK(data).build();
            } catch (DisabledException e) {
                throw new ServiceException(ErrorCode.AUTHENTICATION_EXCEPTION, "User is disabled!");
                //throw new AuthenticationException("User is disabled!", e);
            } catch (BadCredentialsException e) {
                throw new ServiceException(ErrorCode.AUTHENTICATION_EXCEPTION, "Bad credentials!");
                //throw new AuthenticationException("Bad credentials!", e);
            }
        }
        else{
            httpServletResponse.setStatus(403);
            throw new ServiceException(ErrorCode.FORBIDDEN__EXCEPTION,"您无权限登录");
        }
    }
}
