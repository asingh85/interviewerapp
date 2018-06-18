package com.softvision.service;

import com.softvision.entities.Interview;
import java.util.List;
import java.util.Optional;

public interface InterviewService<T extends Interview> {

    Optional<T> getAll();

    Optional<T> getInterviewById(String id);

    void deleteInterview(String id);

    void deleteAllInterviews();

    long getInterviewByCandidateId(String id);

}
