package com.kht.backend.aop;

import com.kht.backend.aop.annotation.MethodLog;
import com.kht.backend.dataobject.OperaLogDO;
import com.kht.backend.service.OperationLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private OperationLogService operationLogService;

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

        operaLogDO.setLogTime(System.currentTimeMillis());
        operaLogDO.setEmployeeCode("");
    }
}
