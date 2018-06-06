package com.softvision.interview.adminservice.config;

import com.softvision.interview.adminservice.controller.InterviewerController;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/")
public class JerseyConfig extends ResourceConfig {
    public JerseyConfig() {
        register(InterviewerController.class);
    }
}
