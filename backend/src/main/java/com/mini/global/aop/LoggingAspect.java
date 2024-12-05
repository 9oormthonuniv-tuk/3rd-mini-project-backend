package com.mini.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private static final ThreadLocal<Integer> callDepth = ThreadLocal.withInitial(() -> 0);

    @Pointcut("within(com.mini.domain..*)")
    public void domainPackagePointcut() {}

    @Around("domainPackagePointcut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        int depth = callDepth.get();
        callDepth.set(depth + 1);

        String indent = new String(new char[depth]).replace("\0", "    ");
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();


        if (log.isDebugEnabled()) {
            log.debug("{}------> {}.{}() with argument[s] = {}", indent, className, methodName, Arrays.toString(joinPoint.getArgs()));
        }

        try {
            Object result = joinPoint.proceed();

            if (log.isDebugEnabled()) {
                log.debug("{}<------ {}.{}() with result = {}", indent, className, methodName, result);
            }
            return result;
        } catch (Exception e) {
            log.error("{}Exception in {}.{}() with cause = {}", indent, className, methodName, e.getCause() != null ? e.getCause() : "NULL");
            throw e;
        } finally {
            callDepth.set(depth);
        }
    }
}
