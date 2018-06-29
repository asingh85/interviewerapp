package com.softvision.mapper;

import com.softvision.constant.InterviewConstant;
import com.softvision.exception.ServiceException;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.model.Interviewlog;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class NextRoundStatus {

    @Inject
    InterviewService interviewService;


    public Optional<Interviewlog> moveToNextInterview(String id, String nextInterviewId) throws ServiceException {

        Interview interview = (Interview) interviewService.getById(id).get();

            LocalDateTime joiningDate = LocalDateTime.now();

            // move ack to previous log
            Interviewlog intLogInterview = new Interviewlog();
            intLogInterview.setInterviewStatus(interview.getInterviewStatus());
            intLogInterview.setCandidateId(interview.getCandidateId());
            intLogInterview.setInterviewerId(interview.getInterviewerId());
            intLogInterview.setModifiedDate(interview.getModifiedDate());
            intLogInterview.setCreationTime(interview.getCreationTime());
            intLogInterview.setInterviewId(interview.getId());
            intLogInterview.setInterviewerList(interview.getInterviewerList());
            intLogInterview.setTechnology(interview.getTechnology());
            interviewService.addInterviewLog(intLogInterview);

            interviewService.deleteInterview(interview.getId());

            Interview ackInterview = new Interview();
            ackInterview.setInterviewStatus(InterviewStatus.INITIATED);
            ackInterview.setCandidateId(interview.getCandidateId());
            ackInterview.setInterviewerId(nextInterviewId);
            ackInterview.setModifiedDate(joiningDate);
            ackInterview.setCreationTime(joiningDate);
            ackInterview.setInterviewerList(null);
            ackInterview.setTechnology(interview.getTechnology());
            return  interviewService.addInterview(ackInterview);

    }
}



