package com.demo1.aop;

import com.demo1.domain.entities.ActivityLog;
import com.demo1.repository.ActivityLogRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
@Aspect
@Component
public class ExecutionTimeAspect {

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Around("@annotation(com.demo1.aop.annotation.ExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - startTime;

        String methodName = joinPoint.getSignature().toShortString();

        ActivityLog logEntry = new ActivityLog();
        logEntry.setDate(new Date());
        logEntry.setOperation(methodName);
        logEntry.setDuration(duration);

        activityLogRepository.save(logEntry);

        return result;
    }
}
