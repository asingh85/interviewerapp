package com.softvision.serviceimpl;

import com.softvision.entities.Interview;
import com.softvision.entities.InterviewStatus;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.StatusInterface;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.stereotype.Service;

@Service
public class NextlevelStatus implements StatusInterface {

    @Inject
    InterviewRepository interviewRepository;

    public final String status = InterviewStatus.NEXTLEVEL.toString();

    public String getStatus() {
        return status;
    }

    @Override
    public Optional<Interview> addInterview(String candidateId,String interviewId){
        LocalDateTime joiningDate =LocalDateTime.now();
        Interview  interview = new Interview();
        interview.setInterviewStatus(InterviewStatus.NEXTLEVEL);
        List interviewList= new ArrayList<String>();
        interviewList.add(interviewId);
        interview.setInterviewerId(interviewList);
        interview.setModifiedDate(joiningDate);
        interview.setCreationTime(joiningDate);
        interview.setCandidateId(candidateId);
        interview.setDeleted(false);
        return Optional.of(interviewRepository.save(interview));
    }

}
