package com.softvision.mapper;

import com.softvision.model.Interview;
import com.softvision.exception.ServiceException;
import java.util.Optional;

public interface  StatusInterface{

    String getStatus();

    Optional<Interview> addInterview(String candidateId,String interviewId) throws ServiceException;
}
