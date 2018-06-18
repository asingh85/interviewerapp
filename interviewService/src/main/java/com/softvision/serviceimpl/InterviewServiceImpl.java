package com.softvision.serviceimpl;

import com.softvision.entities.Interview;
import com.softvision.entities.InterviewStatus;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.InterviewService;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class InterviewServiceImpl implements InterviewService {

    @Inject
    InterviewRepository interviewRepository;

    @Inject
    MongoTemplate mongoTemplate;

    @Override
    public Optional<List<Interview>> getAll() {
        return Optional.of(interviewRepository.findAll());
    }

    @Override
    public Optional<Interview> getInterviewById(String id) {
        return Optional.of(interviewRepository.findById(id).get());
    }

    @Override
    public void deleteInterview(String id) {
        interviewRepository.deleteById(id);
    }

    @Override
    public void deleteAllInterviews() {
        interviewRepository.deleteAll();
    }

    @Override
    public long getInterviewByCandidateId(String candidateId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("candidateId").is(candidateId)
                .andOperator(Criteria.where("interviewStatus").is(InterviewStatus.ACKNOWLEDGED)));
        long count = mongoTemplate.count(query, Interview.class);
        return count;
    }
}
