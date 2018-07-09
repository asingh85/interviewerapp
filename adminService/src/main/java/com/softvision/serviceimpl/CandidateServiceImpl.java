package com.softvision.serviceimpl;

import com.softvision.common.ServiceConstants;
import com.softvision.model.Candidate;
import com.softvision.repository.CandidateRepository;
import com.softvision.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.*;

/**
 * @author arun.p The Class CandidateServiceImpl.
 */
@Service
public class CandidateServiceImpl implements CandidateService {

    /**
     * The candidate repository.
     */
    @Inject
    CandidateRepository candidateRepository;

    /**
     * The mongo template.
     */
    @Inject
    MongoTemplate mongoTemplate;

    /**
     * The Constant LOGGER.
     */
    public static final Logger LOGGER = LoggerFactory.getLogger(CandidateServiceImpl.class);

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#addCandidate(com.softvision.model.
     * Candidate)
     */
    @Override
    public Candidate addCandidate(final Candidate candidate) {

        candidate.setCreatedDate(LocalDateTime.now());
        candidate.setIsActive(true);

        return candidateRepository.insert(candidate);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#findCandidateById(java.lang.String)
     */
    @Override
    public Candidate findCandidateById(final String id) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);
        if (optionalCandidate.isPresent()) {
            return optionalCandidate.get();
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#deleteCandidateById(java.lang.String)
     */
    @Override
    public Candidate deleteCandidateById(final String id) {

        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);

        if (!optionalCandidate.isPresent()) {
            throw new RuntimeException("Employee Not Found!");
        }

        Candidate candidate = optionalCandidate.get();
        candidate.setIsActive(false);
        candidate.setModifiedDate(LocalDateTime.now());
        candidateRepository.save(candidate);
        return new Candidate();

    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#saveAllCandidates(java.util.List)
     */
    @Override
    public List<Candidate> saveAllCandidates(final List<Candidate> candidates) {
        candidates.forEach(candidate -> candidate.setCreatedDate(LocalDateTime.now()));
        return candidateRepository.saveAll(candidates);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#findAllCandidates(org.springframework
     * .data.domain.Pageable)
     */
    @Override
    public List<Candidate> findAllCandidates() {
        return candidateRepository.findAll();
    }

    @Override
    public List<Candidate> findByIsActiveIsTrue() {

        Candidate candidate = new Candidate();
        candidate.setIsActive(true);
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().
                                 withMatcher("isActive", exact());
        Example<Candidate> exCandidate = Example.of(candidate, matcher);
        System.out.println(exCandidate);
        System.out.println(candidateRepository.findAll(exCandidate));
        return candidateRepository.findAll(exCandidate);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#updateCandidate(com.softvision.model.
     * Candidate, java.lang.String)
     */
    @Override
    public Candidate updateCandidate(final Candidate candidate, final String id) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);
        if (optionalCandidate.isPresent()) {
            candidate.setCandidateId(id);
            candidate.setModifiedDate(LocalDateTime.now());
            return candidateRepository.save(candidate);
        }
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see com.softvision.service.CandidateService#deleteAllCandidates()
     */
    @Override
    public void deleteAllCandidates() {
        candidateRepository.deleteAll();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.CandidateService#searchCandidate(java.lang.String)
     */
    @Override
    public List<Candidate> searchCandidate(final String searchAttribute) {

        LOGGER.info(" Search string is : {} ", searchAttribute);
        StringBuilder covertStr = new StringBuilder();
        covertStr.append("/").append(searchAttribute).append("/");
        Criteria criteria = new Criteria();
        criteria = criteria.orOperator(Criteria.where("firstName").regex(searchAttribute, "si"),
                Criteria.where("lastName").regex(searchAttribute, "si"),
                Criteria.where("technologyStack").regex(searchAttribute, "si"),
                Criteria.where("phoneNumber").regex(searchAttribute, "si"),
                Criteria.where("email").regex(searchAttribute, "si"));
        criteria = criteria.andOperator(Criteria.where("isActive").is(true));

        Query query = new Query(criteria);

        System.out.println(query.toString());
        return mongoTemplate.find(query, Candidate.class);
    }

}
