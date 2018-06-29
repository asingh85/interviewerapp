package com.softvision.mapper;

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
public class RejectedStatus {

    @Inject
    InterviewService interviewService;

    public Optional<Interviewlog> rejectCandidate(String id) throws ServiceException {

        Interview interview = (Interview) interviewService.getById(id).get();

        LocalDateTime joiningDate = LocalDateTime.now();

        // move Ack status to previous state
        Interviewlog ackInterviewLog = new Interviewlog();
        ackInterviewLog.setInterviewStatus(interview.getInterviewStatus());
        ackInterviewLog.setCandidateId(interview.getCandidateId());
        ackInterviewLog.setInterviewerId(interview.getInterviewerId());
        ackInterviewLog.setModifiedDate(interview.getModifiedDate());
        ackInterviewLog.setCreationTime(interview.getCreationTime());
        ackInterviewLog.setInterviewId(interview.getId());
        ackInterviewLog.setInterviewerList(interview.getInterviewerList());
        ackInterviewLog.setTechnology(interview.getTechnology());

        interviewService.addInterviewLog(ackInterviewLog);

        // move rejected to previous state
        Interviewlog rejInterview = new Interviewlog();
        rejInterview.setInterviewStatus(InterviewStatus.REJECTED);
        rejInterview.setCandidateId(interview.getCandidateId());
        rejInterview.setInterviewerId(interview.getInterviewerId());
        rejInterview.setModifiedDate(joiningDate);
        rejInterview.setCreationTime(joiningDate);
        rejInterview.setInterviewId(interview.getId());
        rejInterview.setInterviewerList(null);
        rejInterview.setTechnology(interview.getTechnology());

        // all recode with rejected status
        interviewService.deleteInterview(interview.getId());

        return interviewService.addInterviewLog(rejInterview);
    }

    public Optional<Interviewlog> rejectedCount(String interviewId) throws ServiceException {
        return interviewService.getRejectedDetail(interviewId);
    }
}
