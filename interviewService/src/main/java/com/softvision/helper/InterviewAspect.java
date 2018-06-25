package com.softvision.helper;


import com.softvision.exception.ServiceException;
import com.softvision.service.InterviewService;
import javax.inject.Inject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class InterviewAspect {

    public static final Logger logger = LoggerFactory.getLogger(InterviewAspect.class);

    @Inject
    InterviewService interviewService;

    @Around("@annotation(InsertInterviewValidator)")
    public Object permit(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnValue = null;
        String candidateId = (String) joinPoint.getArgs()[0];

        Long count = interviewService.getCandidateCount(candidateId);

        if (count == 0) {
            returnValue = joinPoint.proceed();
        } else {
            throw new ServiceException(" All ready one person has Acknowledged");
        }
        return returnValue;
    }
}
