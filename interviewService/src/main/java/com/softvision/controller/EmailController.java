package com.softvision.controller;

import com.softvision.service.EmailService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;

@Path("email")
public class EmailController {

    @Inject
    EmailService emailService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public void sendMail(@Suspended AsyncResponse asyncResponse) {

        CompletableFuture.runAsync(() -> {
            try {
                emailService.sendMail();
                asyncResponse.resume(Response.status(Response.Status.OK).build());
            } catch (Exception ex) {
                asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build());
            }
            });
    }


}
