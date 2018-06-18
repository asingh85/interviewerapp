package com.softvision.service;

import com.softvision.entities.Interview;
import java.util.Optional;

public interface  StatusInterface{
    String status="";

    String getStatus();

    Optional<Interview> addInterview(String candidateId,String interviverId);
}
