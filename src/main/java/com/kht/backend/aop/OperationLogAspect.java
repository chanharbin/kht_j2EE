package com.kht.backend.aop;

import com.kht.backend.aop.annotation.MethodLog;
import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.service.OperationLogService;
import com.kht.backend.util.JwtTokenProvider;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Pointcut("@annotation(com.kht.backend.aop.annotation.MethodLog)")
    public void logPointCut() {}

    @AfterReturning("logPointCut()")
    public void addOperationLog(JoinPoint joinPoint) {
        OperaLogDO operaLogDO = new OperaLogDO();
        MethodSignature signature = (MethodSignature)joinPoint.getSignature();
        Method method = signature.getMethod();

        MethodLog methodLog = method.getAnnotation(MethodLog.class);
        if (methodLog != null) {
            operaLogDO.setOperaCode(methodLog.value());
        }

        operaLogDO.setLogCode(0);
        operaLogDO.setLogTime(System.currentTimeMillis());
        operaLogDO.setEmployeeCode("1000zxc00001");
        //operaLogDO.setEmployeeCode(jwtTokenProvider.getUserPrincipalFromRequest(httpServletRequest).getCode());

        operationLogService.addOperationLog(operaLogDO);
    }
}
