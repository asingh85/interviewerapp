package com.softvision.repository;

import com.softvision.model.Interviewer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewerRepository extends MongoRepository<Interviewer, String> {

}
