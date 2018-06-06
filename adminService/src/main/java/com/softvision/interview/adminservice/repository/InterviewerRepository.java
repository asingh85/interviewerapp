package com.softvision.interview.adminservice.repository;

import com.softvision.interview.adminservice.model.Interviewer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewerRepository extends MongoRepository<Interviewer,String> {

}
