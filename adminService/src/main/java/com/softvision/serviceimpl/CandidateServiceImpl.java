package com.softvision.serviceimpl;

import com.softvision.common.ServiceConstants;
import com.softvision.model.Candidate;
import com.softvision.repository.CandidateRepository;
import com.softvision.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    public String deleteCandidateById(final String id) {

        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);

        if (!optionalCandidate.isPresent()) {
            return ServiceConstants.CANDIDATE_NOT_FOUND;
        }

        Candidate candidate = optionalCandidate.get();
        candidate.setIsActive(false);
        candidate.setModifiedDate(LocalDateTime.now());
        candidateRepository.save(candidate);
        return ServiceConstants.CANDIDATE_DELETED;

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
        return candidateRepository.findByIsActiveIsTrue();
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
