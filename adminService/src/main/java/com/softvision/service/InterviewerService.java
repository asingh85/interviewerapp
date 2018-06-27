package com.softvision.service;

import com.softvision.model.Interviewer;
import com.softvision.model.TechnologyCommunity;
import java.util.List;
import java.util.Optional;

/**
 * The interface  InterviewerService
 */
public interface InterviewerService<T extends Interviewer> {

    /**
     * Find all interviewers
     *
     * @return the interviewer
     */
    Optional<List<Interviewer>> getAllInterviewer();

    /**
     * Find interviewer by ID
     *
     * @param id the id
     * @return the interviewer
     */
    Optional<T> getInterviewerById(String id);

    /**
     * Searching operation
     *
     * @param id the id
     * @return search result
     */
    List<T> search(String id);

    /**
     * Adding a interviewer
     *
     * @param interviewer the interviewer
     * @return Optional<T>
     */
    Optional<T> addInterviewer(T interviewer);

    /**
     * Updating interviewer by ID
     *
     * @param interviewer the interviewer
     * @param id          the id
     * @return updateInterviewer
     */
    Optional<T> updateInterviewer(T interviewer, String id);

    /**
     * Deleteing interviewer by ID (softDelete)
     *
     * @param id the id
     */
    void deleteInterviewer(String id);

    /**
     * Deleting all interviewers (softDelete)
     */
    void deleteAllInterviewers();

    /**
     * Find all interviewers by band and exp
     *
     * @param expInmonths        the expInmonths
     * @param technicalCommunity the technicalCommunity
     * @return interviewer
     */
    Optional<List<Interviewer>> getAllInterviewerByBandExp(int expInmonths, String technicalCommunity);

    /**
     * Find interviewer by TechStack
     *
     * @return TechnologyCommunity
     */
    Optional<List<TechnologyCommunity>> getTechStack();
}
