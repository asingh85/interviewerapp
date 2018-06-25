package com.softvision.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.softvision.model.Candidate;

// TODO: Auto-generated Javadoc
/**
 * The Interface CandidateService.
 *
 * @author arun.p The Interface CandidateService.
 */
public interface CandidateService {

	/**
	 * Adds the candidate.
	 *
	 * @param candidate
	 *            the candidate
	 * @return the candidate
	 */
	Candidate addCandidate(final Candidate candidate);

	/**
	 * Find candidate by id.
	 *
	 * @param id
	 *            the id
	 * @return the candidate
	 */
	Candidate findCandidateById(final String id);

	/**
	 * Delete candidate by id.
	 *
	 * @param id
	 *            the id
	 * @return the string
	 */
	String deleteCandidateById(final String id);

	/**
	 * Save all candidates.
	 *
	 * @param candidates
	 *            the candidates
	 * @return the list
	 */
	List<Candidate> saveAllCandidates(final List<Candidate> candidates);

	/**
	 * Find all candidates.
	 *
	 * @param pageable
	 *            the pageable
	 * @return the page
	 */
	Page<Candidate> findAllCandidates(final Pageable pageable);

	/**
	 * Update candidate.
	 *
	 * @param candidate
	 *            the candidate
	 * @param id
	 *            the id
	 * @return the candidate
	 */
	Candidate updateCandidate(final Candidate candidate, String id);

	/**
	 * Delete all candidates.
	 */
	void deleteAllCandidates();

	/**
	 * Search candidate.
	 *
	 * @param searchAttribute
	 *            the search attribute
	 * @return the list
	 */
	List<Candidate> searchCandidate(final String searchAttribute);

	/**
	 * Find by is active is true.
	 *
	 * @param page1 the page
	 * @return the page
	 */
	Page<Candidate> findByIsActiveIsTrue(Pageable page);

}
