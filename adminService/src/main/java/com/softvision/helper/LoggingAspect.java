package com.softvision.helper;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Configuration
public class LoggingAspect {

    public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Around("@annotation(com.softvision.helper.Loggable)")
    public Object logTime(ProceedingJoinPoint jp) throws Throwable {
        Instant now = Instant.now();
        Object object = jp.proceed();
        logger.info(" Method execution time for {} method is {}  milliseconds", jp.getSignature().getName(), Duration.between(Instant.now(), now));
        return object;
    }

    @Around("@annotation(com.softvision.helper.Loggable)")
    public Object logInAndout(ProceedingJoinPoint jp) throws Throwable {
        logger.info(" Entering Method {} ", jp.getSignature().getName());
        Object object = jp.proceed();
        logger.info(" Exiting Method {} ", jp.getSignature().getName());
        return object;
    }

}
