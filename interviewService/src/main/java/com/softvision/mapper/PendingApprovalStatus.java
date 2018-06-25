package com.softvision.mapper;

import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.exception.ServiceException;
import com.softvision.service.InterviewService;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class PendingApprovalStatus implements StatusInterface {

    @Inject
    InterviewService interviewService;

    public final String status = InterviewStatus.PENDINGAPPROVAL.toString();

    public String getStatus() {
        return status;
    }

    @Override
    public Optional<Interview> addInterview(String candidateId, String interviewId) throws ServiceException {
        return interviewService.addInterview(candidateId,interviewId,status);
    }

}
