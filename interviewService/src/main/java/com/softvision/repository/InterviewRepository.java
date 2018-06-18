package com.softvision.repository;

import com.softvision.entities.Interview;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterviewRepository extends MongoRepository<Interview,String> {

}
