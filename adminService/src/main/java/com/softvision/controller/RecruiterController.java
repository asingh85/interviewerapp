package com.softvision.controller;

import com.softvision.common.ServiceConstants;
import com.softvision.helper.Loggable;
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
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.discovery.DiscoveryClient;

@Path(ServiceConstants.BACK_SLASH + ServiceConstants.RECRUITER)
public class RecruiterController {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RecruiterController.class);


    /**
     * The recruiterService
     */
    @Inject
    RecruiterService recruiterService;

    /**
     * The discoveryClient
     */
    @Inject
    DiscoveryClient discoveryClient;

    /**
     * getRecruiterDetails by ID
     *
     * @param asyncResponse the asyncResponse
     * @param id            The id
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable

    public void getRecruiterDetails(@Suspended AsyncResponse asyncResponse,
                                    @PathParam("id") String id) {
        LOGGER.info("Eureka instances :{}", discoveryClient.getInstances("recruiter"));
        LOGGER.info("Recruiter ID is : {} ", id);
        CompletableFuture.supplyAsync(() -> recruiterService.getRecruiter(id))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Recruiter ID is [ " + id + " ] not available").build()));
    }

    /**
     * Find All Recruiters based on isDeleted 'true' OR 'false'
     *
     * @param asyncResponse the asyncResponse
     * @param size          the size
     * @param sortOrder     the sortOrder
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllRecruiters(@Suspended AsyncResponse asyncResponse,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sort") String sortOrder,
                                 @QueryParam("isDeleted") boolean isDeleted) {
        LOGGER.info("Number of elements request is {} , sort order is {} and isDeleted is {} ", size, sortOrder, isDeleted);
        if (StringUtils.isEmpty(sortOrder) && size < 1) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Number of elements request should not be 0 and sort order should be given").build());
        } else if (!isDeleted) {
            CompletableFuture.supplyAsync(() -> recruiterService.getAll())
                    .thenApply(v -> (List<Recruiter>) v.get())
                    .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> !p.isDeleted()).collect(Collectors.toList())))
                    .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            CompletableFuture.supplyAsync(() -> recruiterService.getAll())
                    .thenApply(v -> (List<Recruiter>) v.get())
                    .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> p.isDeleted()).collect(Collectors.toList())))
                    .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        }
    }

    /**
     * Adding a recruiter
     *
     * @param asyncResponse the asyncResponse
     * @param recruiter     the recruiter
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void addRecruiter(@Suspended AsyncResponse asyncResponse,
                             Recruiter recruiter) {
        ValidationUtil.validate(recruiter);
        CompletableFuture.supplyAsync(() -> recruiterService.addRecruiter(recruiter))
                .thenApply(recruiter1 -> asyncResponse.resume(recruiter))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Recruiter is not added").build()));
    }


    /**
     * Update recruiter by ID
     *
     * @param asyncResponse the asyncResponse
     * @param recruiter     the recruiter
     * @param id            The id
     */
    @PUT
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void updateRecruiter(@Suspended AsyncResponse asyncResponse,
                                Recruiter recruiter, @PathParam("id") String id) {
        ValidationUtil.validate(recruiter);
        LOGGER.info("Update recruiter {} ", id);
        CompletableFuture.supplyAsync(() -> recruiterService.updateRecruiter(recruiter, id))
                .thenApply(recruiter1 -> asyncResponse.resume(recruiter))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    /**
     * Delete recruiter by ID (softDelete)
     *
     * @param asyncResponse the asyncResponse
     * @param id            The id
     */
    @DELETE
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Loggable
    public void deleteRecruiter(@Suspended AsyncResponse asyncResponse,
                                @PathParam("id") String id) {
        LOGGER.info("Deleting recruiter {} ", id);
        CompletableFuture future = CompletableFuture.runAsync(() -> recruiterService.deleteRecruiter(id));
        asyncResponse.resume(future.join());
    }

    /**
     * delete All Recruiters (softDelete)
     *
     * @param asyncResponse the asyncResponse
     */
    @DELETE
    @Loggable
    public void deleteAllRecruiter(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All recruiters ");
        CompletableFuture future = CompletableFuture.runAsync(() -> recruiterService.deleteAllRecruiter());
        asyncResponse.resume(future.join());
    }


}
