package com.softvision.interview.adminservice.service;

import com.softvision.interview.adminservice.model.Interviewer;
import java.util.List;

public interface InterviewerService <T extends Interviewer> {

    List<Interviewer> getAll();

    T getInterviewer(String id);

    T addInterviewer(T interviewer);

    T updateInterviewer(T interviewer ,String id);

    void deleteInterviewer(String id);

    void deleteAllInterviewers();
}
