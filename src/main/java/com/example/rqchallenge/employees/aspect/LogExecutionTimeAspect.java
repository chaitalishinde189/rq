package com.example.rqchallenge.employees.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * @author chaitali shinde
 * Aspect logging execution time for method annotated with @LogExecutionTime
 */
@Component
@Aspect
@Slf4j
public class LogExecutionTimeAspect {

    @Around("@annotation(com.example.rqchallenge.employees.annotation.LogExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - startTime;

        log.info("Method: {} executed in {} ms", joinPoint.toShortString(), executionTime);
        return proceed;
    }

}
