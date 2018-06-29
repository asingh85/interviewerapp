package com.softvision.repository;

import com.softvision.model.Interviewlog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewLogRepository extends MongoRepository<Interviewlog,String> {
}
