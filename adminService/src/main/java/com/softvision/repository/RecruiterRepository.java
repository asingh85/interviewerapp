package com.softvision.repository;

import com.softvision.model.Recruiter;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecruiterRepository extends MongoRepository<Recruiter, String> {
}
