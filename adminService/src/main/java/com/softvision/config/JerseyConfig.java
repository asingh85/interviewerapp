package com.softvision.config;

import com.softvision.common.ServiceConstants;
import com.softvision.controller.CandidateController;
import com.softvision.controller.InterviewerController;
import com.softvision.controller.LoginController;
import com.softvision.controller.RecruiterController;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath(ServiceConstants.BACK_SLASH + ServiceConstants.ADMIN)
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(InterviewerController.class);
        register(RecruiterController.class);
        register(LoginController.class);
        register(CandidateController.class);
    }
}
