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
import javax.ws.rs.POST;
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
import com.softvision.entities.Interview;

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
                                @QueryParam("interviverId") String interviverId,
                                @QueryParam("status") String status) {
        LOGGER.info(" Called insertInterview -->  candidateId " + candidateId + " interviverId: " + interviverId + " status:" + status);


        System.out.println(" Called count :" + parentInterface.getStatusInterface().length);
        StatusInterface[] abc = parentInterface.getStatusInterface();
        List<StatusInterface> list = Arrays.asList(abc);
        list.stream().forEach(v -> {
                    if (v.getStatus().equalsIgnoreCase(status))
                        CompletableFuture.supplyAsync(() -> v.addInterview(candidateId,interviverId))
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
        if(id != null && !id.isEmpty()) {
            CompletableFuture.supplyAsync(() -> interviewService.getInterviewById(id))
                    .thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        }else{
             asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Interview Id cannot be NULL or Empty").build());
        }
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterview(@Suspended AsyncResponse asyncResponse) {

       /* CompletableFuture.supplyAsync(() -> interviewService
                .getAll())
                .thenApply(v -> new GenericEntity<List<Interview>>(v, Optional.class))
                .thenApply(v -> asyncResponse.resume(v))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build()));
*/
        CompletableFuture.supplyAsync(() -> interviewService
            .getAll())
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                                e.getMessage()).build()));
    }

    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void addInterview(@Suspended AsyncResponse asyncResponse,
                             Interview interview) {
        LOGGER.info(" Called Add" + interview);
        if(interview != null){
            CompletableFuture.supplyAsync(() -> interviewService.addInterview(interview))
                    .thenApply(candidate1 -> asyncResponse.resume(interview));
        }else{
            asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Interview Object cannot be NULL"));
        }

    }

    @DELETE
    @Path("/{id}")
    @Loggable
    public void deleteInterview(@Suspended AsyncResponse asyncResponse,
                                @PathParam("id") String id) {
        LOGGER.info("Deleting Interview {} ", id);
        if(id != null && !id.isEmpty()) {
            CompletableFuture.runAsync(() -> interviewService.deleteInterview(id));
        }else{
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

    @POST
    @Path("/accepted")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void updateAccepted(@Suspended AsyncResponse asyncResponse,
                             @QueryParam("interviewId") String interviewId,
                             @QueryParam("interviewerId") String interviewerId) {
        LOGGER.info(" Called accepted --> interviewId : " + interviewId + "  -interviewerId " + interviewerId);

        if((interviewId != null && !interviewId.isEmpty())&&( interviewerId != null && !interviewerId.isEmpty())) {

            CompletableFuture future = CompletableFuture.supplyAsync(
                    () -> interviewService.updateAccepted(interviewId, interviewerId))
                    .thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                                    e.getMessage()).build()));
        }else{
            asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Interview Id and interviewer Id cannot be NULL or Empty").build());
        }
    }

    @POST
    @Path("/updatestatus")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void updateStatus(@Suspended AsyncResponse asyncResponse,
                               @QueryParam("id") String id,
                               @QueryParam("status") String status) {
        LOGGER.info(" Called accepted --> id : " + id + "  -status " + status);
        if((id != null && !id.isEmpty())&&( status != null && !status.isEmpty())) {
            CompletableFuture future = CompletableFuture.supplyAsync(
                    () -> interviewService.updateInterviewByStatus(id, status))
                    .thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(
                                    e.getMessage()).build()));
        }else{
            asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Interview Id and interviewer Id cannot be NULL or Empty").build());
        }
    }
}