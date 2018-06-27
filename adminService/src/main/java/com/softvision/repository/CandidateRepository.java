package com.softvision.repository;

import java.util.List;

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
	public List<Candidate> findByIsActiveIsTrue();
}
