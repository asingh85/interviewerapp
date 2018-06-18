package com.softvision.config;

import com.softvision.controller.InterviewerController;
import com.softvision.controller.RecruiterController;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/admin")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(InterviewerController.class);
        register(RecruiterController.class);
    }
}
