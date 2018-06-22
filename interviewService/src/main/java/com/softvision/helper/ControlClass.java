package com.softvision.helper;

import com.softvision.mapper.StatusInterface;
import org.springframework.stereotype.Component;

@Component
public class ControlClass implements ControlInterface {

    private StatusInterface[] statusInterface;

    public ControlClass(
            StatusInterface[] statusInterface) {
        this.statusInterface = new StatusInterface[0];
        this.statusInterface =  statusInterface;
    }

    public StatusInterface[] getStatusInterface() {
        return statusInterface;
    }

    public void setStatusInterface(StatusInterface[] statusInterface) {
        this.statusInterface = statusInterface;
    }
}
