package com.softvision.service;

import com.softvision.model.Interview;
import com.softvision.exception.ServiceException;
import com.softvision.model.Interviewlog;
import java.time.temporal.TemporalAmount;
import java.util.List;
import java.util.Optional;
import javax.swing.text.html.Option;

public interface InterviewService<T> {

    Optional<T> addInterview(Interview interview) throws ServiceException;

    Optional<Interviewlog> addInterviewLog(Interviewlog interviewlog) throws ServiceException;

    void deleteInterview(String id) throws ServiceException;

  //  Optional<T> getInterviewByCandidateId(String id,String status) throws ServiceException;

  //  long getCandidateCount(String id) throws ServiceException;

    Optional<List<T>> getAll() throws ServiceException;

    Optional<List<T>> getPendingByInterviewId(String interviewerId) throws ServiceException;

    Optional<T> getById(String id) throws ServiceException;

    void deleteAllInterviews() throws ServiceException;

    Optional<T> getAcknowledgedDetail(String interviewerId,String candidateId) throws ServiceException;

    Optional<List<T>> getAcknowledgedByID(String interviewerId) throws ServiceException;

    Optional<List<Interviewlog>> getRejectedDetail(String interviewerId) throws ServiceException;

    Optional<List<Interviewlog>> getApprovedDetail(String interviewerId) throws ServiceException;

    Optional<List<Interviewlog>> getAllApproved()throws ServiceException;

    Optional<List<Interviewlog>> getAllRejected()throws ServiceException;
}
