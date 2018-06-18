package com.softvision.helper;

import com.softvision.entities.Interview;
import com.softvision.service.StatusInterface;

public interface ControlInterface<T extends Interview>{

    public StatusInterface[] getStatusInterface();

}
