package com.softvision.serviceimpl;

import com.softvision.model.Recruiter;
import com.softvision.repository.RecruiterRepository;
import com.softvision.service.RecruiterService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;


@Component
public class RecruiterServiceImpl implements RecruiterService<Recruiter> {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(RecruiterServiceImpl.class);


    @Inject
    MongoTemplate mongoTemplate;

    @Inject
    RecruiterRepository recruiterRepository;

    @Override
    public Optional<List<Recruiter>> getAll() {
        return Optional.of(recruiterRepository.findAll());
    }

    @Override
    public Optional<Recruiter> getRecruiter(String id) {

        LOGGER.info("RecruiterServiceImpl entered into getRecruiter() ID is :{}", id);
        Optional<Recruiter> optRecruiter = recruiterRepository.findById(id);
        if (optRecruiter.isPresent()) {
            LOGGER.info("RecruiterServiceImpl entered into if (optRecruiter.isPresent())");
            return Optional.of(optRecruiter.get());

        }
        LOGGER.info("RecruiterServiceImpl exit from getRecruiter()");
        return Optional.empty();
    }

    @Override
    public Optional<Recruiter> addRecruiter(Recruiter recruiter) {
        LOGGER.info("Entered into addRecruiter() ");
        if (recruiter != null) {
            LOGGER.info("Recruiter is not null");
            LocalDateTime loc = LocalDateTime.now();
            recruiter.setCreatedDate(loc);
            recruiter.setModifiedDate(loc);
        }
        LOGGER.info("Exit from addRecruiter() ");
        return Optional.of(recruiterRepository.insert(recruiter));
    }

    @Override
    public Optional<Recruiter> updateRecruiter(Recruiter recruiter, String id) {
        LOGGER.info("RecruiterServiceImpl updateRecruiter()  ID is :{}", id);
        Optional<Recruiter> recruiterDAO = recruiterRepository.findById(id);
        if (recruiterDAO.isPresent()) {
            LOGGER.info("RecruiterServiceImpl updateRecruiter()  is not empty");
            recruiter.setId(id);
            recruiter.setCreatedDate(recruiterDAO.get().getCreatedDate());
            recruiter.setModifiedDate(LocalDateTime.now());
            return Optional.of(recruiterRepository.save(recruiter));
        }
        LOGGER.info("RecruiterServiceImpl updateRecruiter()  Exit");
        return null;
    }

    @Override
    public void deleteRecruiter(String id) {
        LOGGER.info("RecruiterServiceImpl deleteRecruiter()  ID is :{}", id);
        Optional<Recruiter> recruiterDAO = recruiterRepository.findById(id);
        if (recruiterDAO.isPresent()) {
            LOGGER.info("RecruiterServiceImpl deleteRecruiter()  is not empty");
            Recruiter optRecruiter = recruiterDAO.get();
            optRecruiter.setDeleted(true);
            optRecruiter.setModifiedDate(LocalDateTime.now());
            recruiterRepository.save(optRecruiter);

        }
        LOGGER.info("RecruiterServiceImpl exit from deleteRecruiter()");
    }

    @Override
    public void deleteAllRecruiter() {
        LOGGER.info("RecruiterServiceImpl entered into deleteAllRecruiter()  ");
        List<Recruiter> recruiterList = recruiterRepository.findAll();
        recruiterList.forEach(recruiter ->
                recruiter.setDeleted(true)
        );
        recruiterRepository.saveAll(recruiterList);
        LOGGER.info("RecruiterServiceImpl exit from deleteAllRecruiter()  ");
    }
}
