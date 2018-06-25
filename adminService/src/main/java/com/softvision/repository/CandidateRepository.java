package com.softvision.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softvision.model.Candidate;

/**
 * @author arun.p
 * The Interface CandidateRepository.
 *
 */
@Repository
public interface CandidateRepository extends MongoRepository<Candidate, String> {

	/**
	 * Find by is active is true.
	 *
	 * @param pageble the pageble
	 * @return the page
	 */
	public Page<Candidate> findByIsActiveIsTrue(final Pageable pageble);
}
