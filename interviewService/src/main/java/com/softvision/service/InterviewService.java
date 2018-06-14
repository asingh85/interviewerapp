package com.softvision.service;

import com.softvision.entities.Interview;
import java.util.List;
import java.util.Optional;

public interface InterviewService<T extends Interview> {

    Optional<T> getAll();

    Optional<T> getInterviewById(String id);

    Optional<T> addInterview(T interview);

    void deleteInterview(String id);

    void deleteAllInterviews();

    Optional<T> updateInterviewByStatus(String id,String status);

    Optional<T> updateAccepted(String id,String interviewId);

}
