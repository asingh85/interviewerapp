package com.softvision.controller;

import com.softvision.common.ServiceConstants;
import com.softvision.model.Candidate;
import com.softvision.service.CandidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author arun.p
 * The Class CandidateController.
 */
@Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATE_SERVICE)
public class CandidateController {

    /**
     * The candidate service.
     */
    @Inject
    private CandidateService candidateService;

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CandidateController.class);

    /**
     * Adds the candidate.
     *
     * @param asyncResponse the async response
     * @param candidate     the candidate
     */
    @POST
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATE + ServiceConstants.BACK_SLASH + ServiceConstants.ADD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addCandidate(@Suspended final AsyncResponse asyncResponse,
                             final Candidate candidate) {
        LOGGER.info("In addCandidate() :: Saving the candidate to DB");
        CompletableFuture.supplyAsync(() -> candidateService.addCandidate(candidate))
                .thenApply(candidate1 -> asyncResponse.resume(candidate));
    }

    /**
     * Adds the all candidates.
     *
     * @param asyncResponse the async response
     * @param candidates    the candidates
     */
    @POST
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATES + ServiceConstants.BACK_SLASH
            + ServiceConstants.ADD)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addAllCandidates(@Suspended final AsyncResponse asyncResponse, final List<Candidate> candidates) {
        LOGGER.info("In addAllCandidates() :: Saving all the candidates to DB");
        CompletableFuture<List<Candidate>> future = CompletableFuture
                .supplyAsync(() -> candidateService.saveAllCandidates(candidates));
        asyncResponse.resume(future.join());
    }

    /**
     * Find candidate by id.
     *
     * @param asyncResponse the async response
     * @param id            the id
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATE + ServiceConstants.BACK_SLASH
            + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Produces(MediaType.APPLICATION_JSON)
    public void findCandidateById(@Suspended final AsyncResponse asyncResponse, @PathParam("id") final String id) {
        LOGGER.info("In findCandidateById() :: Fetching candidate details for {} ", id);
        CompletableFuture<Candidate> future = CompletableFuture
                .supplyAsync(() -> candidateService.findCandidateById(id));
        asyncResponse.resume(future.join());
    }

    /**
     * Delete candidate by id.
     *
     * @param asyncResponse the async response
     * @param id            the id
     */
    @DELETE
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATE + ServiceConstants.BACK_SLASH
            + ServiceConstants.DELETE + ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET
            + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Produces(MediaType.APPLICATION_JSON)
    public void deleteCandidateById(@Suspended final AsyncResponse asyncResponse, @PathParam("id") final String id) {
        LOGGER.info("In deleteCandidateById() :: Deleting candidate {} ", id);
        CompletableFuture<String> future = CompletableFuture
                .supplyAsync(() -> candidateService.deleteCandidateById(id));
        asyncResponse.resume(Response.status(Response.Status.OK).entity(future.join()).build());

    }

    /**
     * Update candidate.
     *
     * @param asyncResponse the async response
     * @param candidate     the candidate
     * @param id            the id
     */
    @PUT
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATE + ServiceConstants.BACK_SLASH
            + ServiceConstants.UPDATE + ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET
            + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void updateCandidate(@Suspended final AsyncResponse asyncResponse, final Candidate candidate,
                                @PathParam("id") final String id) {
        LOGGER.info("In updateCandidate() :: Updating candidate {} ", id);
        CompletableFuture.supplyAsync(() -> candidateService.updateCandidate(candidate, id))
                .thenApply(candidate1 -> asyncResponse.resume(candidate));
    }

    /**
     * Gets the all candidate details.
     *
     * @param asyncResponse the async response
     * @param page          the page
     * @param size          the size
     * @param sortBy        the sort by
     * @param sortOrder     the sort order
     * @return the all candidate details
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATES)
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllCandidateDetails(@Suspended final AsyncResponse asyncResponse/*, @QueryParam("page") final int page,
			@QueryParam("size") final int size, @QueryParam("sortBy") final String sortBy,
			@QueryParam("sortOrder") final String sortOrder*/) {

        LOGGER.info("In getAllCandidateDetails() :: Getting all candidates {} ");

	/*	final Pageable resultPage = (Pageable) PageRequest.of(page, size,
				(sortOrder.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy);*/
        final CompletableFuture<List<Candidate>> future = CompletableFuture
                .supplyAsync(() -> candidateService.findByIsActiveIsTrue());
        asyncResponse.resume(future.join());
    }


    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.ALL_CANDIDATES)
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllCandidateForAdmin(@Suspended final AsyncResponse asyncResponse/*, @QueryParam("page") final int page,
			@QueryParam("size") final int size, @QueryParam("sortBy") final String sortBy,
			@QueryParam("sortOrder") final String sortOrder*/) {

        LOGGER.info("In getAllCandidateDetails() :: Getting all candidates {} ");

		/*final Pageable resultPage = (Pageable) PageRequest.of(page, size,
				(sortOrder.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC), sortBy);*/
        final CompletableFuture<List<Candidate>> future = CompletableFuture
                .supplyAsync(() -> candidateService.findAllCandidates());
        asyncResponse.resume(future.join());
    }


    /**
     * Search candidate.
     *
     * @param searchAttribute the search attribute
     * @param asyncResponse   the async response
     */
    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.CANDIDATE + ServiceConstants.BACK_SLASH
            + ServiceConstants.SEARCH)
    @Produces(MediaType.APPLICATION_JSON)
    public void searchCandidate(@QueryParam("searchAttribute") final String searchAttribute,
                                @Suspended final AsyncResponse asyncResponse) {

        LOGGER.info("In searchCandidate() :: Searching candidates {} for :", searchAttribute);

        final CompletableFuture<List<Candidate>> future = CompletableFuture
                .supplyAsync(() -> candidateService.searchCandidate(searchAttribute));
        asyncResponse.resume(future.join());
    }

}
