package com.softvision.mapper;

import com.softvision.constant.InterviewConstant;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.exception.ServiceException;
import com.softvision.service.InterviewService;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class AcknowledgedStatus implements StatusInterface {

    @Inject
    InterviewService interviewService;

    public final String status = InterviewStatus.ACKNOWLEDGED.toString();

    public String getStatus() {
        return status;
    }

    @Override
    public Optional<Interview> addInterview(String candidateId, String interviewId) throws ServiceException {
        Long count = interviewService.getCandidateCount(candidateId);
        if (count != 0) {
            throw  new ServiceException(InterviewConstant.CANDIDATE_ACK);
        } else {
            return interviewService.addInterview(candidateId, interviewId, status);
        }
    }
}



