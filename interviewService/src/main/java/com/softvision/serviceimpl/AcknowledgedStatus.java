package com.softvision.serviceimpl;

import com.softvision.entities.Interview;
import com.softvision.entities.InterviewStatus;
import com.softvision.service.StatusInterface;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AcknowledgedStatus implements StatusInterface {

    public final String status = InterviewStatus.ACKNOWLEDGED.toString();

    public String getStatus() {
        return status;
    }

    @Override
    public Optional<Interview> addInterview(){
        System.out.println(" called  AcknowledgedStatus doSomething()");
        return null;
    }

}
