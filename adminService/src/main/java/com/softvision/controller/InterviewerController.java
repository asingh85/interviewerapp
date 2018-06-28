package com.softvision.controller;

import com.softvision.common.ServiceConstants;
import com.softvision.helper.Loggable;
import com.softvision.model.Interviewer;
import com.softvision.model.TechnologyCommunity;
import com.softvision.service.InterviewerService;
import com.softvision.validation.ValidationUtil;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

@Path(ServiceConstants.BACK_SLASH + ServiceConstants.INTERVIEWER)
public class InterviewerController {

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewerController.class);

    /**
     * The interviewerService.
     */
    @Inject
    InterviewerService interviewerService;

    /**
     * getInterviewer By Id
     *
     * @param asyncResponse the async response
     * @param id            the id
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getInterviewerById(@Suspended AsyncResponse asyncResponse,
                                   @PathParam("id") String id) {
        LOGGER.info("Candidate ID is : {} ", id);
        if (StringUtils.isEmpty(id)) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Interviewer Id  cannot be NULL or Empty.").build());

        }
        CompletableFuture.supplyAsync(() -> interviewerService.getInterviewerById(id))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    /**
     * search operation
     *
     * @param asyncResponse the async response
     * @param str           the str
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.SEARCH)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void search(@Suspended AsyncResponse asyncResponse,
                       @QueryParam("str") String str) {
        LOGGER.info("Search string is  : {} ", str);
        if (StringUtils.isEmpty(str)) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Search string cannot be NULL or Empty.").build());

        }
        CompletableFuture.supplyAsync(() -> interviewerService.search(str))
                .thenApply(list -> asyncResponse.resume(list))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    /**
     * Find all interviewers based on isDeleted 'true' OR 'false'
     *
     * @param asyncResponse the async response
     * @param size          the size
     * @param sortOrder     the sortOrder
     * @param isDeleted     the isDeleted
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterviewer(@Suspended AsyncResponse asyncResponse,
                                  @QueryParam("size") Integer size,
                                  @QueryParam("sort") String sortOrder,
                                  @QueryParam("isDeleted") boolean isDeleted) {

        LOGGER.info("Number of elements request is {} and sort order is {} and isDeleted {} ", size, sortOrder, isDeleted);
        if (StringUtils.isEmpty(sortOrder) && size < 1) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Number of elements request should not be 0 and sort order should be given").build());
        } else if (!isDeleted) {
            CompletableFuture.supplyAsync(() -> interviewerService.getAllInterviewer())
                    .thenApply(v -> (List<Interviewer>) v.get())
                    .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> !p.isDeleted()).collect(Collectors.toList())))
                    .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            CompletableFuture.supplyAsync(() -> interviewerService.getAllInterviewer())
                    .thenApply(v -> (List<Interviewer>) v.get())
                    .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> p.isDeleted()).collect(Collectors.toList())))
                    .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        }
    }


    /**
     * Adds the interviewer
     *
     * @param asyncResponse the async response
     * @param interviewer   the interviewer
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void addInterviewer(@Suspended AsyncResponse asyncResponse,
                               Interviewer interviewer) {
        ValidationUtil.validate(interviewer);
        CompletableFuture.supplyAsync(() -> interviewerService.addInterviewer(interviewer))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()));
    }

    /**
     * Update interviewer with ID
     *
     * @param asyncResponse the async response
     * @param interviewer   the interviewer
     * @param id            the id
     */
    @PUT
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void updateInterviewer(@Suspended AsyncResponse asyncResponse,
                                  Interviewer interviewer, @PathParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity("Input missing").build());
        }
        ValidationUtil.validate(interviewer);
        CompletableFuture.supplyAsync(() -> interviewerService.updateInterviewer(interviewer, id))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()));

    }

    /**
     * Delete interveiwer by ID (softDelete)
     *
     * @param asyncResponse the async response
     * @param id            the id
     */
    @DELETE
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Loggable
    public void deleteInterviewer(@Suspended AsyncResponse asyncResponse,
                                  @PathParam("id") String id) {

        LOGGER.info("Deleting candidate {} ", id);
        CompletableFuture future = CompletableFuture.runAsync(() -> interviewerService.deleteInterviewer(id));
        asyncResponse.resume(future.join());
    }

    /**
     * Delete All Interviewers (softDelete)
     *
     * @param asyncResponse the async response
     */
    @DELETE
    @Loggable
    public void deleteAllInterviewer(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All candidates ");

        CompletableFuture future = CompletableFuture.runAsync(() -> interviewerService.deleteAllInterviewers());
        Response.ResponseBuilder rb = Response.ok("the test response");
        Response response = rb.header("Access-Control-Allow-Origin", "*")
                .status(Response.Status.BAD_REQUEST).entity("Input missing").build();
        asyncResponse.resume(response);
    }

    /**
     * getAllInterviewers By Band And Exp
     *
     * @param asyncResponse       the async response
     * @param technologyCommunity the technologyCommunity
     * @param bandExperience      the bandExperience
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.GET_BY_BAND_EXP)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterviewerByBandExp(@Suspended AsyncResponse asyncResponse,
                                           @QueryParam("tc") String technologyCommunity,
                                           @QueryParam("be") int bandExperience) {

        if (StringUtils.isEmpty(technologyCommunity) && bandExperience < 3) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Technology Community should not be empty and the Band Experience should also not be empty").build());
        }
        LOGGER.info("Technology Community is {} and as per the Band Experience is {} ", technologyCommunity, bandExperience);
        CompletableFuture<Optional<List<Interviewer>>> future = CompletableFuture
                .supplyAsync(() -> interviewerService.getAllInterviewerByBandExp(bandExperience, technologyCommunity));
        Optional<List<Interviewer>> interviewer = future.join();
        if (interviewer.isPresent()) {
            asyncResponse.resume(interviewer.get().stream()
                    .sorted(Comparator.comparing(Interviewer::getBandExperience))
                    .collect(Collectors.toList()));
        }
    }


    /**
     * getTechStack
     *
     * @param asyncResponse the async response
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.GET_TECH)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getTechStack(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture<Optional<List<TechnologyCommunity>>> future = CompletableFuture
                .supplyAsync(() -> interviewerService.getTechStack());
        asyncResponse.resume(future.join().get().stream()
                .sorted()
                .collect(Collectors.toList()));
    }
}
