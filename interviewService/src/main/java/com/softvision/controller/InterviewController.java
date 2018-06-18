package com.softvision.controller;

import com.softvision.helper.ControlInterface;
import com.softvision.helper.Loggable;
import com.softvision.service.InterviewService;
import com.softvision.service.StatusInterface;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("interview")
public class InterviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewController.class);

    @Inject
    InterviewService interviewService;

    @Inject
    ControlInterface parentInterface;

    @GET
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void insertInterview(@Suspended AsyncResponse asyncResponse,
                                @QueryParam("candidateId") String candidateId,
                                @QueryParam("interviewId") String interviewId,
                                @QueryParam("status") String status) {
        LOGGER.info(" Called insertInterview -->  candidateId " + candidateId + " interviewId: " + interviewId + " status:" + status);
        StatusInterface[] abc = parentInterface.getStatusInterface();
        List<StatusInterface> list = Arrays.asList(abc);
        list.stream().forEach(v -> {
                    if (v.getStatus().equalsIgnoreCase(status))
                        CompletableFuture.supplyAsync(() -> v.addInterview(candidateId, interviewId))
                                .thenApply(optional -> asyncResponse.resume(optional.get()))
                                .exceptionally(e -> asyncResponse.resume(
                                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
                }
        );
    }


    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getInterviewById(@Suspended AsyncResponse asyncResponse,
                                 @PathParam("id") String id) {
        LOGGER.info("Interview ID is : {} ", id);
        if (id != null && !id.isEmpty()) {
            CompletableFuture.supplyAsync(() -> interviewService.getInterviewById(id))
                    .thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Interview Id cannot be NULL or Empty").build());
        }
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterview(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> interviewService
                .getAll())
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                                e.getMessage()).build()));
    }

    @DELETE
    @Path("/{id}")
    @Loggable
    public void deleteInterview(@Suspended AsyncResponse asyncResponse,
                                @PathParam("id") String id) {
        LOGGER.info("Deleting Interview {} ", id);
        if (id != null && !id.isEmpty()) {
            CompletableFuture.runAsync(() -> interviewService.deleteInterview(id));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Interview Id cannot be NULL or Empty").build());
        }
    }

    @DELETE
    @Path("/all")
    @Loggable
    public void deleteAllInterview(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All Interviews ");
        CompletableFuture future = CompletableFuture.runAsync(() -> interviewService.deleteAllInterviews());
        asyncResponse.resume(future.join());
    }
}