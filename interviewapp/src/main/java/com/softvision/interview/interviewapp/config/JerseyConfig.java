package com.softvision.interview.interviewapp.config;

import com.softvision.interview.interviewapp.controller.InterviewerController;
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
