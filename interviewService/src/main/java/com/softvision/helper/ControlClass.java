package com.softvision.helper;

import com.softvision.service.StatusInterface;
import org.springframework.stereotype.Component;

@Component
public class ControlClass implements ControlInterface {

    public StatusInterface[] statusInterface = new StatusInterface[0];

    public ControlClass(
            StatusInterface[] statusInterface) {
        this.statusInterface =  statusInterface;
    }

    public StatusInterface[] getStatusInterface() {
        return statusInterface;
    }

    public void setStatusInterface(StatusInterface[] statusInterface) {
        this.statusInterface = statusInterface;
    }
}
