package com.softvision.service;

import com.softvision.model.Recruiter;
import java.util.List;
import java.util.Optional;

/**
 * The interface RecruiterService
 */
public interface RecruiterService<T extends Recruiter> {

    /**
     * Find recruiters
     *
     * @return Recruiter
     */
    Optional<List<Recruiter>> getAll();

    /**
     * Find recruiter by ID
     *
     * @param id the id
     * @return Recruiter
     */
    Optional<T> getRecruiter(String id);

    /**
     * @param recruiter the recruiter
     * @return recruiter
     */
    Optional<T> addRecruiter(T recruiter);

    /**
     * Update recruiter by ID
     *
     * @param recruiter the recruiter
     * @param id        the id
     * @return updateRecruiter
     */
    Optional<T> updateRecruiter(T recruiter, String id);

    /**
     * Delete Recruiter by ID (softDelete)
     *
     * @param id the id
     */
    void deleteRecruiter(String id);

    /**
     * Delete All Recruiters (softDelete)
     */
    void deleteAllRecruiter();


}
