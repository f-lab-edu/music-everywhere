package me.kong.groupservice.common.aop;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.groupservice.common.annotation.UserNameLock;
import me.kong.groupservice.domain.repository.UserNameLockDAO;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class UserNameLockAop {

    private final UserNameLockDAO lockDAO;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(me.kong.groupservice.common.annotation.UserNameLock)")
    public Object lock(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        UserNameLock lock = method.getAnnotation(UserNameLock.class);

        String key = lock.key();
        log.info("key : {}", key);

        try {
            lockDAO.getLock(key, lock.leaseTime());
            return aopForTransaction.proceed(joinPoint);
        } finally {
            lockDAO.releaseLock(key);
        }
    }
}
