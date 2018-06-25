package com.softvision.service;

import com.softvision.model.Interviewer;
import com.softvision.model.Login;

import java.util.List;
import java.util.Optional;

public interface InterviewerService <T extends Interviewer> {

    Optional<List<Interviewer>> getAllInterviewer();

    Optional<T> getInterviewerById(String id);

    List<T> search(String  id);

    Optional<T> addInterviewer(T interviewer);

    Optional<T> updateInterviewer(T interviewer ,String id);

    void deleteInterviewer(String id);

    void deleteAllInterviewers();

    Optional<List<Interviewer>> getAllInterviewerByBandExp(int expInmonths , String technicalCommunity);
}
