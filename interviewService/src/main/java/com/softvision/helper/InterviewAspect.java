package com.softvision.helper;


import com.softvision.entities.Interview;
import com.softvision.repository.InterviewRepository;
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
    InterviewRepository interviewRepository;

    @Around("@annotation(InsertInterviewValidator)")
    public Object permit(ProceedingJoinPoint  joinPoint) throws Throwable {
        System.out.println(" ******called InterviewAspect *****");
        System.out.println(joinPoint.getArgs()[0]);
        String interviewId=(String)joinPoint.getArgs()[0];

        Interview interview= interviewRepository.findById(interviewId).get();
        if(interview != null && interview.getAcceptedCount() <=1){
            throw new Exception();
        }else{
            return joinPoint.proceed();
        }
    }
}
