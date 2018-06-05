package com.softvision.interview.interviewapp.service;

import com.softvision.interview.interviewapp.model.Interviewer;
import java.util.List;
import java.util.Optional;

public interface InterviewerService <T extends Interviewer> {

    List<Interviewer> getAll();

    T getInterviewer(String id);

    T addInterviewer(T interviewer);

    T updateInterviewer(T interviewer ,String id);

    void deleteInterviewer(String id);

    void deleteAllInterviewers();
}
