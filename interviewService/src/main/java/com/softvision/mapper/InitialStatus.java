package com.softvision.mapper;

import com.softvision.exception.ServiceException;
import com.softvision.model.*;
import com.softvision.service.EmailService;
import com.softvision.service.InterviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class InitialStatus {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialStatus.class);

    @Inject
    InterviewService interviewService;


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

        return addedInterview;

    }



}
