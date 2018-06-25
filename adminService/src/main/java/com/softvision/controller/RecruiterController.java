package com.softvision.controller;

import com.softvision.model.Recruiter;
import com.softvision.service.RecruiterService;
import com.softvision.validation.ValidationUtil;
import java.util.List;
import java.util.concurrent.CompletableFuture;
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
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Path("/recruiter")
public class RecruiterController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RecruiterController.class);


    @Inject
    RecruiterService recruiterService;

    @Inject
    DiscoveryClient discoveryClient;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void getRecruiterDetails(@Suspended AsyncResponse asyncResponse,
                                    @PathParam("id") String id) {
        LOGGER.info("Eureka instances :{}", discoveryClient.getInstances("recruiter"));
        LOGGER.info("Recruiter ID is : {} ", id);
        CompletableFuture.supplyAsync(() -> recruiterService.getRecruiter(id))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Recruiter ID is [ " + id + " ] not available").build()));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllRecruiters(@Suspended AsyncResponse asyncResponse,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sort") String sortOrder) {

        LOGGER.info("Number of elements request is {} and sort order is {} ", size, sortOrder);
        CompletableFuture.supplyAsync(() -> recruiterService.getAll())
                .thenApply(v -> (List<Recruiter>) v.get())
                .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> !p.isDeleted()).collect(Collectors.toList())))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @Path("/getAll")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllOnlyFalse(@Suspended AsyncResponse asyncResponse,
                                @QueryParam("size") Integer size,
                                @QueryParam("sort") String sortOrder) {

        LOGGER.info("Number of elements request is {} and sort order is {} ", size, sortOrder);
        CompletableFuture.supplyAsync(() -> recruiterService.getAllOnlyFalse())
                .thenApply(v -> (List<Recruiter>) v.get())
                .thenApply(k -> asyncResponse.resume(k.stream().sorted().collect(Collectors.toList())))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));


    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addRecruiter(@Suspended AsyncResponse asyncResponse,
                             Recruiter recruiter) {
        ValidationUtil.validate(recruiter);
        CompletableFuture.supplyAsync(() -> recruiterService.addRecruiter(recruiter))
                .thenApply(recruiter1 -> asyncResponse.resume(recruiter))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Recruiter is not added").build()));
    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateRecruiter(@Suspended AsyncResponse asyncResponse,
                                Recruiter recruiter, @PathParam("id") String id) {
        ValidationUtil.validate(recruiter);
        CompletableFuture.supplyAsync(() -> recruiterService.updateRecruiter(recruiter, id))
                .thenApply(recruiter1 -> asyncResponse.resume(recruiter))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @DELETE
    @Path("/{id}")
    public void deleteRecruiter(@Suspended AsyncResponse asyncResponse,
                                @PathParam("id") String id) {
        LOGGER.info("Deleting recruiter {} ", id);
        CompletableFuture future = CompletableFuture.runAsync(() -> recruiterService.deleteRecruiter(id));
        asyncResponse.resume(future.join());
    }

    @DELETE
    @Path("/all")
    public void deleteAllRecruiter(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All recruiters ");
        CompletableFuture future = CompletableFuture.runAsync(() -> recruiterService.deleteAllRecruiter());
        asyncResponse.resume(future.join());
    }


}
