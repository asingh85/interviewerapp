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
public class ApprovedStatus {

    @Inject
    InterviewService interviewService;

    public Optional<Interview> approveCandidate(String id, String nextInterviewerId ,String interviewerType) throws ServiceException {
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
        interviewService.addInterviewLog(approveInterview);

        if(interviewerType.equalsIgnoreCase(InterviewConstant.MANAGER)){
            // TODO : Need to come up with the data need to passed
            return Optional.of(new Interview());
        }else{
            interview.setId(null);
            interview.setInterviewerId(nextInterviewerId);
            interview.setInterviewStatus(InterviewStatus.INITIATED);
            return interviewService.addInterview(interview);
        }
    }

    public Optional<Interviewlog> approvedCount(String interviewerId) throws ServiceException {
        return interviewService.getApprovedDetail(interviewerId);
    }
   }