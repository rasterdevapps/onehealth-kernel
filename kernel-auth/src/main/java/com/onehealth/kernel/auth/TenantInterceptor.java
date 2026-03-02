package com.onehealth.kernel.auth;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantInterceptor {

    @Around("execution(* com.onehealth.kernel..*.find*(..))")
    public Object validateTenantContext(ProceedingJoinPoint joinPoint) throws Throwable {
        if (TenantContext.getTenantId() == null) {
            throw new IllegalStateException("Tenant context is not set");
        }
        return joinPoint.proceed();
    }
}
