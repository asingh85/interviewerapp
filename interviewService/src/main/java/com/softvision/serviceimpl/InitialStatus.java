package com.softvision.serviceimpl;

import com.softvision.entities.Interview;
import com.softvision.entities.InterviewStatus;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.StatusInterface;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class InitialStatus implements StatusInterface {

    @Inject
    InterviewRepository interviewRepository;

    public  final String status = InterviewStatus.INITIATED.toString();

    public String getStatus() {
        return status;
    }

    @Override
    public Optional<Interview> addInterview(String candidateId,String interviverId){
        LocalDateTime joiningDate =LocalDateTime.now();
        Interview  interview = new Interview();
        interview.setInterviewStatus(InterviewStatus.INITIATED);
        interview.setInterviewerId(null);
        interview.setModifiedDate(joiningDate);
        interview.setCreationTime(joiningDate);
        interview.setCandidateId(candidateId);
        interview.setDeleted(false);
      //  Interview  interview = new Interview(candidateId,InterviewStatus.INITIATED,joiningDate,joiningDate,false);
        return Optional.of(interviewRepository.save(interview));
    }

}
