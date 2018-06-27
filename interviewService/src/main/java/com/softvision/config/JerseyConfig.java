package com.softvision.config;

import com.softvision.controller.InterviewController;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

@Configuration
@ApplicationPath("/interview")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {
        register(InterviewController.class);
    }
}
