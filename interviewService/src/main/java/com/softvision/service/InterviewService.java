package com.softvision.service;

import com.softvision.model.Interview;
import com.softvision.exception.ServiceException;
import java.util.Optional;

public interface InterviewService<T extends Interview> {

    Optional<T> getAll() throws ServiceException;

    Optional<T> getInterviewById(String id) throws ServiceException;

    void deleteInterview(String id) throws ServiceException;

    void deleteAllInterviews() throws ServiceException;

    long getCandidateCount(String id) throws ServiceException;

    Optional<T> addInterview(String candidateId,String interviewId,String status) throws ServiceException;

}
