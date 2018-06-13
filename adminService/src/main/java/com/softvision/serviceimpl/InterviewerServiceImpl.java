package com.softvision.serviceimpl;

import com.softvision.model.Interviewer;
import com.softvision.repository.InterviewerRepository;
import com.softvision.service.InterviewerService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class InterviewerServiceImpl implements InterviewerService<Interviewer> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(InterviewerServiceImpl.class);

    @Inject
    private InterviewerRepository interviewerRepository;

    @Inject
    MongoTemplate mongoTemplate;

    @Override
    public Optional<List<Interviewer>> getAllInterviewer() {
        return Optional.of(interviewerRepository.findAll());
    }

    @Override
    public Optional<Interviewer> getInterviewerById(String id) {
        LOGGER.info("InterviewerServiceImpl ID is : {} ", id);
        return Optional.of(interviewerRepository.findById(id).get());
    }

    @Override
    public Optional<Interviewer> addInterviewer(Interviewer interviewer) {
        LocalDateTime localDateTime = LocalDateTime.now();
        interviewer.setCreatedDate(localDateTime);
        interviewer.setModifiedDate(localDateTime);
        return Optional.of(interviewerRepository.insert(interviewer));
    }

    @Override
    public Optional<Interviewer> updateInterviewer(Interviewer interviewer, String id) {
        Optional<Interviewer> interviewerDAO = interviewerRepository.findById(id);
        interviewer.setId(id);
        LocalDateTime localDateTime = LocalDateTime.now();
        interviewer.setCreatedDate(localDateTime);
        interviewer.setModifiedDate(localDateTime);
        return Optional.of(interviewerRepository.save(interviewer));
    }

    @Override
    public void deleteInterviewer(String id) {
        interviewerRepository.deleteById(id);
    }

    @Override
    public void deleteAllInterviewers() {
        interviewerRepository.deleteAll();
    }

    @Override
    public Optional<List<Interviewer>> getAllInterviewerByBandExp(int expInmonths, String technicalCommunity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("technologyCommunity").is(technicalCommunity)
                .andOperator(Criteria.where("bandExperience").gte(expInmonths)));
        List<Interviewer> interviewers = mongoTemplate.find(query, Interviewer.class);
        return Optional.of(interviewers);
    }
}
