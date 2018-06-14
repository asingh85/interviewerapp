package com.softvision.config;

import com.softvision.controller.InterviewController;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(InterviewController.class);
    }
}
