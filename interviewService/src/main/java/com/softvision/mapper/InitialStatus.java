package com.softvision.mapper;

import com.netflix.discovery.EurekaClient;
import com.softvision.exception.ServiceException;
import com.softvision.helper.ServiceClientHelper;
import com.softvision.model.Candidate;
import com.softvision.model.Employee;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    ServiceClientHelper serviceClientHelper;

    public Optional<Interview> publishInterview(String candidateId, int experience, String technology) throws ServiceException {

        Collection<Employee> interviewerList = serviceClientHelper.getInterviewers(technology, experience);
        Candidate candidate = serviceClientHelper.getCandidateById(candidateId);

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
        interview.setCandidateId(candidateId);
        interview.setTechnology(technology);
        Optional<Interview> addedInterview=  interviewService.addInterview(interview);

        sendmail(interviewerList,candidate);
        return addedInterview;
    }

    private void sendmail(Collection<Employee> employeeList, Candidate candidate) {
       // TODO need to get candidate information and send mail to all the interviers
    }
}
