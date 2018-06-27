package com.softvision.serviceimpl;

import com.softvision.model.Interviewer;
import com.softvision.model.TechnologyCommunity;
import com.softvision.repository.InterviewerRepository;
import com.softvision.service.InterviewerService;
import java.time.LocalDateTime;
import java.util.Arrays;
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

    /**
     * (non-Javadoc)
     *
     * @see com.softvision.service.InterviewerService#getInterviewerById(String)
     */
    @Override
    public Optional<Interviewer> getInterviewerById(String id) {
        LOGGER.info("InterviewerServiceImpl ID is : {} ", id);
        return Optional.of(interviewerRepository.findById(id).get());
    }

    /**
     * (non-Javadoc)
     *
     * @see com.softvision.service.InterviewerService#search(String)
     */
    @Override
    public List<Interviewer> search(String str) {

        LOGGER.info(" Search string is : {} ", str);
        StringBuilder covertStr = new StringBuilder();
        covertStr.append("/").append(str).append("/");
        Criteria criteria = new Criteria();
        criteria = criteria.orOperator(
                Criteria.where("firstName").regex(str, "si")
                , Criteria.where("lastName").regex(str, "si")
                , Criteria.where("technologyCommunity").regex(str, "si")
                , Criteria.where("interviewerID").regex(str, "si"));


        Query query = new Query(criteria);

        System.out.println(query.toString());
        return mongoTemplate.find(query, Interviewer.class);
    }

    /**
     * (non-Javadoc)
     *
     * @see com.softvision.service.InterviewerService#addInterviewer(Interviewer)
     */
    @Override
    public Optional<Interviewer> addInterviewer(Interviewer interviewer) {
        LOGGER.info(" Entered into addInterviewer() ");
        LocalDateTime localDateTime = LocalDateTime.now();
        interviewer.setCreatedDate(localDateTime);
        interviewer.setModifiedDate(localDateTime);
        LOGGER.info(" Exit  from addInterviewer() ");
        return Optional.of(interviewerRepository.insert(interviewer));
    }

    /**
     * (non-Javadoc)
     *
     * @see com.softvision.service.InterviewerService#updateInterviewer(Interviewer, String)
     */
    @Override
    public Optional<Interviewer> updateInterviewer(Interviewer interviewer, String id) {
        LOGGER.info("InterviewerServiceImpl updateRecruiter()  ID is :{}", id);
        Optional<Interviewer> interviewerDAO = interviewerRepository.findById(id);
        if (interviewerDAO.isPresent()) {
            interviewer.setId(id);
            LocalDateTime localDateTime = LocalDateTime.now();
            interviewer.setCreatedDate(localDateTime);
            interviewer.setModifiedDate(localDateTime);
            LOGGER.info("InterviewerServiceImpl updateRecruiter()  Exit");
            return Optional.of(interviewerRepository.save(interviewer));
        }
        return Optional.empty();
    }

    /**
     * (non-Javadoc)
     *
     * @see InterviewerService#deleteInterviewer(String)
     */
    @Override
    public void deleteInterviewer(String id) {
        LOGGER.info("InterviewerServiceImpl deleteInterviewer()  ID is :{}", id);
        Optional<Interviewer> interviewerDAO = interviewerRepository.findById(id);
        if (interviewerDAO.isPresent()) {
            LOGGER.info("InterviwerServiceImpl deleteRecruiter()  is not empty");
            Interviewer optInterviwer = interviewerDAO.get();
            optInterviwer.setDeleted(true);
            optInterviwer.setModifiedDate(LocalDateTime.now());
            interviewerRepository.save(optInterviwer);
        }
        LOGGER.info("InterviewerServiceImpl exit from deleteRecruiter()");
    }

    /**
     * (non-Javadoc)
     *
     * @see InterviewerService#deleteAllInterviewers()
     */
    @Override
    public void deleteAllInterviewers() {
        LOGGER.info("InterviewerServiceImpl entered into deleteAllRecruiter()  ");
        List<Interviewer> interviewerList = interviewerRepository.findAll();
        interviewerList.forEach(interviewer -> interviewer.setDeleted(true));
        interviewerRepository.saveAll(interviewerList);
        LOGGER.info("InterviewerServiceImpl exit from deleteAllRecruiter()  ");
    }

    /**
     * (non-Javadoc)
     *
     * @see com.softvision.service.InterviewerService#getAllInterviewerByBandExp(int, String)
     */
    @Override
    public Optional<List<Interviewer>> getAllInterviewerByBandExp(int expInmonths, String technicalCommunity) {
        Query query = new Query();
        query.addCriteria(Criteria.where("technologyCommunity").is(technicalCommunity)
                .andOperator(Criteria.where("bandExperience").gte(expInmonths)));
        List<Interviewer> interviewers = mongoTemplate.find(query, Interviewer.class);
        LOGGER.info("Interviewers information {} :", interviewers);
        return Optional.of(interviewers);
    }

    /**
     * (non-Javadoc)
     *
     * @see InterviewerService#getTechStack()
     */
    @Override
    public Optional<List<TechnologyCommunity>> getTechStack() {
        List<TechnologyCommunity> list = Arrays.asList(TechnologyCommunity.values());
        return Optional.of(list);
    }
}