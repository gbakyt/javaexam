package kz.gaziz.java.exam.service;

import jakarta.servlet.http.HttpServletRequest;
import kz.gaziz.java.exam.exception.ThrottlingException;
import kz.gaziz.java.exam.model.RequestThrottlingModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Service;

@Aspect
@Slf4j
@Service
@RequiredArgsConstructor
public class ThrottlingFilterService {
    private final RequestThrottlingModel requestThrottlingModel;
    private final HttpServletRequest httpServletRequest;

    @Around("@annotation(kz.gaziz.java.exam.annotation.Limit)")
    public Object aroundLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!requestThrottlingModel.allowRequest(httpServletRequest.getRemoteAddr())) {
            log.error("502 BAD_GATEWAY: TOO MANY REQUESTS");
            throw new ThrottlingException("TOO MANY REQUESTS IN A GIVEN TIME!");
        }
        return joinPoint.proceed();
    }
}
