package com.softvision.interview.interviewapp.repository;

import com.softvision.interview.interviewapp.model.Interviewer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewerRepository extends MongoRepository<Interviewer,String> {

}
