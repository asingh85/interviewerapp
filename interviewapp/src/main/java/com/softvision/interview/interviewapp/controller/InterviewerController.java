package com.softvision.interview.interviewapp.controller;

import com.softvision.interview.interviewapp.model.Interviewer;
import com.softvision.interview.interviewapp.service.InterviewerService;
import com.softvision.interview.interviewapp.validation.ValidationUtil;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.data.mongodb.core.MongoTemplate;

@Path("/interviewer")
public class InterviewerController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewerController.class);

    @Inject
    DiscoveryClient discoveryClient;

    @Inject
    InterviewerService interviewerService;

    /*@GET
    @Path("/welcome")
    @Produces(MediaType.APPLICATION_JSON)
    public String getWelcome(){
        return "Welcome";
    }*/

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getInterviewerDetails(@Suspended AsyncResponse asyncResponse,
                                    @PathParam("id") String id) {
        System.out.println("Eureka instances :" + discoveryClient.getInstances("interviewer"));
        LOGGER.info("Candidate ID is : {} ", id);
        CompletableFuture<Interviewer> future = CompletableFuture.supplyAsync(() -> interviewerService.getInterviewer(id));
        asyncResponse.resume(future.join());
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllCandidateDetails(@Suspended AsyncResponse asyncResponse,
                                       @QueryParam("size") Integer size,
                                       @QueryParam("sort") String sortOrder) {

        LOGGER.info("Number of elements request is {} and sort order is {} ", size,sortOrder );
        CompletableFuture<List<Interviewer>> future = CompletableFuture.supplyAsync(() -> interviewerService.getAll());
        List<Interviewer> interviewersList = future.join();
        if(sortOrder.equals("desc")) {
            asyncResponse.resume(interviewersList.stream()
                    .sorted(Comparator.reverseOrder())
                    .limit(size)
                    .collect(Collectors.toList()));
        } else {
            asyncResponse.resume(interviewersList.stream()
                    .sorted(Comparator.naturalOrder())
                    .limit(size)
                    .collect(Collectors.toList()));
        }

    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addInterviewer(@Suspended AsyncResponse asyncResponse,
                             Interviewer interviewer) {

        ValidationUtil.validate(interviewer);
        CompletableFuture.supplyAsync( () -> interviewerService.addInterviewer(interviewer))
                .thenApply(interviewer1 -> asyncResponse.resume(interviewer));
    }


    @Path("/update/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateInterviewer(@Suspended AsyncResponse asyncResponse,
                               Interviewer interviewer , @PathParam("id") String id) {

        ValidationUtil.validate(interviewer);
        CompletableFuture.supplyAsync( () -> interviewerService.updateInterviewer(interviewer , id))
                .thenApply(interviewer1 -> asyncResponse.resume(interviewer));
    }


    @DELETE
    @Path("/{id}")
    public void deleteInterviewer(@Suspended AsyncResponse asyncResponse,
                                @PathParam("id") String id) {

        LOGGER.info("Deleting candidate {} ", id);
        CompletableFuture future  = CompletableFuture.runAsync(() -> interviewerService.deleteInterviewer(id));
        asyncResponse.resume(future.join());
    }

    @DELETE
    @Path("/all")
    public void deleteAllInterviewer(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All candidates " );
        CompletableFuture future  = CompletableFuture.runAsync(() -> interviewerService.deleteAllInterviewers());
        asyncResponse.resume(future.join());
    }
}
