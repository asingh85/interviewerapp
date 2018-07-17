package com.softvision.serviceimpl;

import com.softvision.exception.ServiceException;
import com.softvision.helper.Loggable;
import com.softvision.model.Interview;
import com.softvision.model.InterviewStatus;
import com.softvision.model.Interviewlog;
import com.softvision.repository.InterviewLogRepository;
import com.softvision.repository.InterviewRepository;
import com.softvision.service.InterviewService;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class InterviewServiceImpl implements InterviewService<Interview> {

    @Inject
    InterviewRepository interviewRepository;

    @Inject
    InterviewLogRepository interviewLogRepository;

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
    public Optional<List<Interview>> getPendingByInterviewId(String interviewerId) throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = criteria.where("interviewStatus").is(InterviewStatus.INITIATED)
                    .orOperator(Criteria.where("interviewerId").is(interviewerId),
                            Criteria.where("interviewerList").is(interviewerId) );

            Query query = new Query(criteria);
            return Optional.of(mongoTemplate.find(query,Interview.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<Interview> getById(String id) throws ServiceException {
        try {
            return interviewRepository.findById(id);
        }catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void deleteInterview(String id) throws ServiceException {
        try {
            interviewRepository.deleteById(id);
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
    public Optional<Interview> addInterview(Interview interview) throws ServiceException {
        try {
            return Optional.of(interviewRepository.save(interview));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Loggable
    @Override
    public Optional<Interview> getAcknowledgedDetail(String interviewerId,String candidateId) throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = criteria.andOperator(Criteria.where("interviewerId").is(interviewerId),
                                  Criteria.where("interviewStatus").is(InterviewStatus.ACKNOWLEDGED.toString()),
                    Criteria.where("candidateId").is(candidateId));


            Query query = new Query(criteria);
        return Optional.of(mongoTemplate.findOne(query, Interview.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Interview>> getAcknowledgedByID(String interviewerId) throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = criteria.andOperator(Criteria.where("interviewerId").is(interviewerId),
                    Criteria.where("interviewStatus").is(InterviewStatus.ACKNOWLEDGED));
            Query query = new Query(criteria);
            return Optional.of(mongoTemplate.find(query, Interview.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Interviewlog>> getRejectedDetail(String interviewerId) throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = criteria.andOperator(Criteria.where("interviewerId").is(interviewerId),
                    Criteria.where("interviewStatus").is(InterviewStatus.REJECTED));
            Query query = new Query(criteria);
            return Optional.of(mongoTemplate.find(query, Interviewlog.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Interviewlog>> getApprovedDetail(String interviewerId) throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = criteria.andOperator(Criteria.where("interviewerId").is(interviewerId),
                    Criteria.where("interviewStatus").is(InterviewStatus.APPROVED));
            Query query = new Query(criteria);
            return Optional.of(mongoTemplate.find(query, Interviewlog.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Interviewlog>> getAllApproved() throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = Criteria.where("interviewStatus").is(InterviewStatus.APPROVED);
            Query query = new Query(criteria);
            return Optional.of(mongoTemplate.find(query, Interviewlog.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<List<Interviewlog>> getAllRejected() throws ServiceException {
        try {
            Criteria criteria = new Criteria();
            criteria = Criteria.where("interviewStatus").is(InterviewStatus.REJECTED);
            Query query = new Query(criteria);
            return Optional.of(mongoTemplate.find(query, Interviewlog.class));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Optional<Interviewlog> addInterviewLog(Interviewlog interviewlog) throws ServiceException {
        try {
            return Optional.of(interviewLogRepository.save(interviewlog));
        } catch (DataAccessResourceFailureException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }
    }


}
