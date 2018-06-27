package com.softvision.mapper;

import com.softvision.exception.ServiceException;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class InitialStatus {

    @Inject
    InterviewService interviewService;


    public Optional<Interview> publishInterview(String candidateId, int experience, String technology) throws ServiceException {

        //TODO  fetch intervier list from admin service passing candidate and exp

        List<String> interviewIdList=new ArrayList();
        interviewIdList.add("6224");
        interviewIdList.add("6269");
        interviewIdList.add("2006");

        LocalDateTime joiningDate = LocalDateTime.now();
        Interview interview = new Interview();
        interview.setInterviewStatus(InterviewStatus.INITIATED);
        interview.setInterviewerId(null);
        interview.setInterviewerList(interviewIdList);
        interview.setModifiedDate(joiningDate);
        interview.setCreationTime(joiningDate);
        interview.setCandidateId(candidateId);
        return interviewService.addInterview(interview);

    }
}
