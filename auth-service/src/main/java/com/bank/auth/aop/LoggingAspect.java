package com.bank.auth.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(* com.bank.auth.service..*(..))")
    public void serviceMethods() {}

    @Pointcut("execution(* com.bank.auth.controller..*(..))")
    public void controllerMethods() {}

    @Before("serviceMethods() || controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Executing: {} with args: {}", 
            joinPoint.getSignature().toShortString(), 
            Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "serviceMethods() || controllerMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Completed: {} returned: {}", 
            joinPoint.getSignature().toShortString(), 
            result != null ? result.getClass().getSimpleName() : "null");
    }

    @AfterThrowing(pointcut = "serviceMethods() || controllerMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        log.error("Exception in: {} with message: {}", 
            joinPoint.getSignature().toShortString(), 
            error.getMessage());
    }

    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.info("{} executed in {} ms", joinPoint.getSignature().toShortString(), executionTime);
        return result;
    }
}
