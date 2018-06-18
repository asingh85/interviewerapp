package com.softvision.serviceimpl;

import com.softvision.entities.Interview;
import com.softvision.entities.InterviewStatus;
import com.softvision.helper.InsertInterviewValidator;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.swing.text.html.Option;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Inject
    InterviewRepository interviewRepository;


    @Override
    public Optional<List<Interview>> getAll() {
        return Optional.of(interviewRepository.findAll());
    }

    @Override
    public Optional<Interview> getInterviewById(String id) {
        return Optional.of( interviewRepository.findById(id).get());
    }

    @Override
  //  @Transactional(isolation = Isolation.READ_COMMITTED)
    public Optional<Interview> addInterview(Interview interview) {
              LocalDateTime joiningDate =LocalDateTime.now();
            interview.setInterviewStatus(InterviewStatus.INITIATED);
            interview.setCreationTime(joiningDate);
            interview.setModifiedDate(joiningDate);
        return Optional.of(interviewRepository.save(interview));
    }

    @Override
    public void deleteInterview(String id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public void deleteAllInterviews() {
        interviewRepository.deleteAll();
    }

    @Override
    public Optional<Interview> updateInterviewByStatus(String id, String status) {
        Interview interview = interviewRepository.findById(id).get();
        if(interview != null) {
            interview.setInterviewStatus(InterviewStatus.valueOf(status));
            interview.setModifiedDate(LocalDateTime.now());
            return Optional.of(interviewRepository.save(interview));
        }else{
            return Optional.empty();
        }
    }

    @Override
    @InsertInterviewValidator
    public Optional<Interview> updateAccepted(String interviewId, String interviewerId) {
        System.out.println(" ----- called updateAccepted ------");
        Interview interview = interviewRepository.findById(interviewId).get();
        if(interview != null) {
            interview.setInterviewStatus(InterviewStatus.ACKNOWLEDGED);
            interview.setModifiedDate(LocalDateTime.now());
            interview.setInterviewerId(Arrays.asList(interviewerId));
            return Optional.of(interviewRepository.save(interview));
        }else{
            return Optional.empty();
        }
    }
}
