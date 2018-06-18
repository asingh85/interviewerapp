package com.softvision.helper;


import com.softvision.entities.Interview;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.InterviewService;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import javax.inject.Inject;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
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
    public Object permit(ProceedingJoinPoint  joinPoint) throws Throwable {

        System.out.println(" InsertInterviewValidator ");
        String candidateId=(String)joinPoint.getArgs()[0];

        System.out.println(" candidateId "+candidateId);

      Long count = interviewService.getInterviewByCandidateId(candidateId);
        System.out.println(" count "+count);
        if(count >=1 ){
            throw new Exception(" All ready one person has Acknowledged");
        }else{
            return joinPoint.proceed();
        }
    }
}
