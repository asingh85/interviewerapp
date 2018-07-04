package com.softvision.mapper;

import com.softvision.exception.ServiceException;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.model.Interviewlog;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class ApprovedStatus {

    @Inject
    InterviewService interviewService;

    public Optional<Interview> interviewerApprove(String id, String nextInterviewerId) throws ServiceException {
        Interview interview = (Interview) interviewService.getById(id).get();
        updateInterviewLog(interview);
        interview.setId(null);
        interview.setInterviewerId(nextInterviewerId);
        interview.setInterviewStatus(InterviewStatus.INITIATED);
        return interviewService.addInterview(interview);
    }

    public Optional<Interviewlog> managerApprove(String id, String nextInterviewerId) throws ServiceException {
        Interview interview = (Interview) interviewService.getById(id).get();
        return updateInterviewLog(interview);
    }

    public Optional<Interviewlog> approvedCount(String interviewerId) throws ServiceException {
        return interviewService.getApprovedDetail(interviewerId);
    }

    private Optional updateInterviewLog(Interview interview) {

        LocalDateTime joiningDate = LocalDateTime.now();

        // move Ack status to previous state
        Interviewlog ackInterviewLog = new Interviewlog();
        ackInterviewLog.setInterviewStatus(interview.getInterviewStatus());
        ackInterviewLog.setCandidateId(interview.getCandidateId());
        ackInterviewLog.setInterviewerId(interview.getInterviewerId());
        ackInterviewLog.setModifiedDate(interview.getModifiedDate());
        ackInterviewLog.setCreationTime(interview.getCreationTime());
        ackInterviewLog.setInterviewId(interview.getInterviewerId());
        ackInterviewLog.setInterviewerList(interview.getInterviewerList());
        ackInterviewLog.setTechnology(interview.getTechnology());

        interviewService.addInterviewLog(ackInterviewLog);

        // move approved to previous state
        Interviewlog approveInterview = new Interviewlog();
        approveInterview.setInterviewStatus(InterviewStatus.APPROVED);
        approveInterview.setCandidateId(interview.getCandidateId());
        approveInterview.setInterviewerId(interview.getInterviewerId());
        approveInterview.setModifiedDate(joiningDate);
        approveInterview.setCreationTime(joiningDate);
        approveInterview.setInterviewId(interview.getId());
        approveInterview.setInterviewerList(null);
        approveInterview.setTechnology(interview.getTechnology());
        // all recode with rejected status
        interviewService.deleteInterview(interview.getId());
        return interviewService.addInterviewLog(approveInterview);
    }


    public Optional<List<Interviewlog>> getAllApproved() throws ServiceException {
        return interviewService.getAllApproved();
      }
}