package com.softvision.serviceimpl;

import com.softvision.constant.InterviewConstant;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.exception.ServiceException;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.InterviewService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.dao.DataAccessResourceFailureException;
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
    public Optional<List<Interview>> getAll() throws ServiceException {
        try {
            return Optional.of(interviewRepository.findAll());
        }catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<Interview> getInterviewById(String id) throws ServiceException {
        try {
            return interviewRepository.findById(id);
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteInterview(String id) throws ServiceException {
        try {
            Optional<Interview> interview = interviewRepository.findById(id);
            if (interview.isPresent()) {
                Interview interviewRec = interview.get();
                interviewRec.setDeleted(true);
                interviewRepository.save(interviewRec);
            } else {
                throw new ServiceException(InterviewConstant.INVALID_INTERVIEW);
            }
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteAllInterviews() throws ServiceException {
        try {
            interviewRepository.deleteAll();
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public long getCandidateCount(String candidateId) throws ServiceException {
        try {
            Query query = new Query();
            query.addCriteria(Criteria.where("candidateId").is(candidateId)
                    .andOperator(Criteria.where("interviewStatus").is(InterviewStatus.ACKNOWLEDGED)));
            return mongoTemplate.count(query, Interview.class);
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional addInterview(String candidateId, String interviewId, String status) throws ServiceException {
        try {
            LocalDateTime joiningDate = LocalDateTime.now();
            Interview interview = new Interview();
            interview.setInterviewStatus(InterviewStatus.valueOf(status));
            List interviewList = new ArrayList<String>();
            interviewList.add(interviewId);
            interview.setInterviewerId(interviewList);
            interview.setModifiedDate(joiningDate);
            interview.setCreationTime(joiningDate);
            interview.setCandidateId(candidateId);
            interview.setDeleted(false);
            return Optional.of(interviewRepository.save(interview));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }
}
