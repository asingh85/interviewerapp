package com.softvision.config;

import com.softvision.common.ServiceConstants;
import com.softvision.controller.CandidateController;
import com.softvision.controller.EmployeeController;
import com.softvision.controller.LoginController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import javax.ws.rs.ApplicationPath;

@Configuration
@ApplicationPath(ServiceConstants.BACK_SLASH + ServiceConstants.ADMIN)
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(EmployeeController.class);
        register(LoginController.class);
        register(CandidateController.class);
    }
}
