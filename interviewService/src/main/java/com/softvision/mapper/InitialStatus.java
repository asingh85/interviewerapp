package com.softvision.mapper;

import com.netflix.discovery.EurekaClient;
import com.softvision.constant.ServiceConstants;
import com.softvision.exception.ServiceException;
import com.softvision.helper.ServiceClientHelper;
import com.softvision.model.*;
import com.softvision.service.EmailService;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitialStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialStatus.class);

    @Inject
    InterviewService interviewService;

    @Inject
    EmailService emailService;



    public Optional<Interview> publishInterview( Collection<Employee> interviewerList, Candidate candidate) throws ServiceException {

        List<String> interviewIds = new ArrayList<>();
        List<String> interviewEmail = new ArrayList<>();
        if (interviewerList != null && !interviewerList.isEmpty()) {
            interviewIds= interviewerList.stream().filter(employee -> employee.getEmployeeId() != null)
                     .map(employee -> employee.getEmployeeId()).collect(Collectors.toList());
        }
        System.out.println(interviewIds);
        LocalDateTime joiningDate = LocalDateTime.now();
        Interview interview = new Interview();
        interview.setInterviewStatus(InterviewStatus.INITIATED);
        interview.setInterviewerId(null);
        interview.setInterviewerList(interviewIds);
        interview.setModifiedDate(joiningDate);
        interview.setCreationTime(joiningDate);
        interview.setCandidateId(candidate.getCandidateId());
        interview.setTechnology(candidate.getTechnologyStack());
        Optional<Interview> addedInterview=  interviewService.addInterview(interview);

        Email email = buildEmail().apply(interviewerList);
        System.out.println(email.getSubject());
        System.out.println(email.getToRecipients());
        System.out.println(emailService.sendEmail(email));

        return addedInterview;

    }

    private Function<Collection<Employee>,Email> buildEmail() {
        return (e -> {

            Email email = new Email();
            String employeeList = e.stream()
                                    .map(v -> v.getEmailId())
                                    .collect(Collectors.joining(","));

            email.setToRecipients("krishnakumar.arjunan@softvision.com");
            email.setSubject(ServiceConstants.CANDIDATE_PUBLISHED);
            email.setTemplateName("published");
            return email;
        });
    }

}
