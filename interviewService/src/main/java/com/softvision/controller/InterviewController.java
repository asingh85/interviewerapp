package com.softvision.controller;

import com.softvision.constant.InterviewConstant;
import com.softvision.constant.ServiceConstants;
import com.softvision.exception.ServiceException;
import com.softvision.helper.Loggable;
import com.softvision.helper.ServiceClientHelper;
import com.softvision.mapper.AcknowledgedStatus;
import com.softvision.mapper.ApprovedStatus;
import com.softvision.mapper.InitialStatus;
import com.softvision.mapper.NextRoundStatus;
import com.softvision.mapper.RejectedStatus;
import com.softvision.model.*;
import com.softvision.repository.InterviewLogRepository;
import com.softvision.service.EmailService;
import com.softvision.service.InterviewService;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.swing.text.html.Option;
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
import org.thymeleaf.context.Context;

@Path("/screen")
public class InterviewController {

    private static final Logger LOGGER = LoggerFactory.getLogger(InterviewController.class);

    @Inject
    InterviewService interviewService;

    @Inject
    RejectedStatus rejectedStatus;

    @Inject
    AcknowledgedStatus acknowledgedStatus;

    @Inject
    InitialStatus initialStatus;

    @Inject
    ApprovedStatus approvedStatus;

    @Inject
    NextRoundStatus nextRoundStatus;

    @Inject
    ServiceClientHelper serviceClientHelper;

    @Inject
    EmailService emailService;


    @GET
    @Path("/publish")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void publish(@Suspended AsyncResponse asyncResponse,
                        @QueryParam("candidateId") String candidateId,
                        @QueryParam("candidateExp") int candidateExp,
                        @QueryParam("technology") String technology) {
        if ((candidateId != null && !candidateId.isEmpty())
                && (technology != null && !technology.isEmpty()) && candidateExp > 0) {

            CompletableFuture<Collection<Employee>> completableFutureinterviewerList = CompletableFuture.supplyAsync( () -> {
                try{
                    return serviceClientHelper.getInterviewers(technology, candidateExp);
                } catch ( Exception ex) {
                   return Collections.emptyList();
                }

            });

            CompletableFuture<Candidate> completableFutureCandidate = CompletableFuture.supplyAsync( ()-> {
                try {
                    return serviceClientHelper.getCandidateById(candidateId);
                } catch (Exception ex) {
                    return null;
                }

            });

            Collection<Employee> interviewerList = completableFutureinterviewerList.join();
            Candidate candidate = completableFutureCandidate.join();

            CompletableFuture.supplyAsync(() -> {
                                 return initialStatus.publishInterview(interviewerList,candidate);
                            })
                            .thenApply(optional -> asyncResponse.resume(optional.get()))
                            .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()))
                            .thenApply(v -> {
                                Map<String,Object> map = publishDataBuilder().apply(baseDataBuilder().apply(candidate));
                                Context context = buildContext().apply(map);
                                return emailService.sendEmail(buildEmail(context,"published").apply(interviewerList));
                            })
                            .exceptionally( e -> {
                                LOGGER.error("Error while sending email {}", e.getMessage());
                                return null;
                            });
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }

    @GET
    @Path("/pending/{interviewerId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getInterviewById(@Suspended AsyncResponse asyncResponse,
                                 @PathParam("interviewerId") String interviewerId) {
        LOGGER.info("Interview ID is : {} ", interviewerId);
        if (interviewerId != null && !interviewerId.isEmpty()) {
            CompletableFuture.supplyAsync(() -> {
                try {
                    return interviewService.getPendingByInterviewId(interviewerId);
                } catch (ServiceException e) {
                    LOGGER.error(e.getMessage());
                    throw e;
                }
            })
                    .thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.NULL_INTERVIEW).build());
        }
    }

    @GET
    @Path("/ack/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getInterviewAckById(@Suspended AsyncResponse asyncResponse,
                                    @PathParam("id") String id) {
        LOGGER.info("Interview ID is : {} ", id);
        if (id != null && !id.isEmpty()) {
            CompletableFuture.supplyAsync(() -> {
                try {
                    return acknowledgedStatus.getAcknowledgedByID(id);
                } catch (ServiceException e) {
                    LOGGER.error(e.getMessage());
                    throw e;
                }
            })
                    .thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.NULL_INTERVIEW).build());
        }
    }

    @GET
    @Path("/reject/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void rejectedCount(@Suspended AsyncResponse asyncResponse,
                              @PathParam("id") String id) {
        LOGGER.info("Called rejected candidateId is : {}", id);
        if (id != null && !id.isEmpty()) {
            CompletableFuture.supplyAsync(() -> {
                return rejectedStatus.rejectedCount(id);
            }).thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }


    @GET
    @Path("/approve/{interviewerId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void approvedCount(@Suspended AsyncResponse asyncResponse,
                              @PathParam("interviewerId") String interviewerId) {
        if (interviewerId != null && !interviewerId.isEmpty()) {
            CompletableFuture.supplyAsync(() -> {
                return approvedStatus.approvedCount(interviewerId);
            }).thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }


    @GET
    @Path("/acknowledge")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void acknowledged(@Suspended AsyncResponse asyncResponse,
                             @QueryParam("id") String id,
                             @QueryParam("interviewerId") String interviewerId) {
        if ((id != null && !id.isEmpty()) && (interviewerId != null && !interviewerId.isEmpty())
                ) {
            CompletableFuture.supplyAsync(() -> acknowledgedStatus.acknowledgedInterview(id, interviewerId))
                            .thenApply(optional -> asyncResponse.resume(optional.get()))
                            .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));

        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }

    @GET
    @Path("/reject")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void rejected(@Suspended AsyncResponse asyncResponse,
                         @QueryParam("id") String id) {
        LOGGER.info("Called rejected candidateId is : {}", id);
        if (id != null && !id.isEmpty()) {
            CompletableFuture.supplyAsync(() -> {
                return rejectedStatus.rejectCandidate(id);
            }).thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }


    @GET
    @Path("/approve")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void approved(@Suspended AsyncResponse asyncResponse,
                         @QueryParam("id") String id,
                         @QueryParam("nextInterviewerId") String nextInterviewerId,
                         @QueryParam("interviewerType") String interviewerType) {
        if ((id != null && !id.isEmpty())
                && (nextInterviewerId != null && !nextInterviewerId.isEmpty())
                && (interviewerType != null && !interviewerType.isEmpty())) {
            CompletableFuture.supplyAsync(() -> {
                if (interviewerType.equalsIgnoreCase(InterviewConstant.MANAGER)) {
                    return approvedStatus.managerApprove(id, nextInterviewerId);
                } else {
                    return approvedStatus.interviewerApprove(id, nextInterviewerId);
                }

            }).thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }

    @GET
    @Path("/approvedall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void approvedAll(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
                    List<Interviewlog> interLogList = approvedStatus.getAllApproved().get();
                    Map<String, Interviewlog> map = interLogList.stream()
                            .collect(HashMap<String, Interviewlog>::new,
                                    (k, v) -> {
                                        k.compute(v.getCandidateId(), (key, value) -> {
                                            if (k.get(v.getCandidateId()) != null) {
                                                if (v.getCreationTime().compareTo(value.getCreationTime()) == -1) {
                                                    k.put(v.getCandidateId(), value);
                                                }
                                            } else {
                                                k.put(v.getCandidateId(), v);
                                            }
                                            return v;
                                        });
                                    },
                                    (k, v) -> k.putAll(v));
                    return Optional.of(map.values());
                }
        ).thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path("/rejectedall")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void rejectedAll(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
                    return rejectedStatus.getAllRejected();
                }
        ).thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path("/next")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void nextRound(@Suspended AsyncResponse asyncResponse,
                          @QueryParam("id") String id,
                          @QueryParam("nextInterviewId") String nextInterviewId) {
        if ((id != null && !id.isEmpty())
                && (nextInterviewId != null && !nextInterviewId.isEmpty())) {
            CompletableFuture.supplyAsync(() -> {
                return nextRoundStatus.moveToNextInterview(id, nextInterviewId);
            }).thenApply(optional -> asyncResponse.resume(optional.get()))
                    .exceptionally(e -> asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.BAD_REQUEST).entity(InterviewConstant.INVALID_REQUEST).build());
        }
    }

    @GET
    @Path("all")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterview(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            try {
                return interviewService.getAll();
            } catch (ServiceException e) {
                LOGGER.error(e.getMessage());
                throw e;
            }
        })
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e ->
                        asyncResponse.resume(
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
            CompletableFuture.runAsync(() -> {
                try {
                    interviewService.deleteInterview(id);
                } catch (ServiceException e) {
                    asyncResponse.resume(
                            Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
                }
            });
        } else {
            asyncResponse.resume(
                    Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(InterviewConstant.NULL_INTERVIEW).build());
        }
    }

    @DELETE
    @Path("/all")
    @Loggable
    public void deleteAllInterview(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All Interviews ");
        CompletableFuture.runAsync(() ->
        {
            try {
                interviewService.deleteAllInterviews();
                asyncResponse.resume(Response.status(Response.Status.OK).entity("Deleted Successfully!").build());
            } catch (ServiceException e) {
                asyncResponse.resume(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build());
            }

        });
    }

    private Function<Collection<Employee>,Email> buildEmail(Context context,String templateType) {
        return (e -> {

            Email email = new Email();
            email.setFrom("arjunankrishnakumar1@gmail.com");
            List<String> list = e.stream().map(v -> v.getEmailId()).collect(Collectors.toList());
//            email.setToRecipients(list.toArray(new String[list.size()]));
            email.setToRecipients(new String[] {"krishnakumar.arjunan@softvision.com"});
            email.setSubject(ServiceConstants.CANDIDATE_PUBLISHED);
            email.setTemplateName(templateType);
            email.setContext(context);
            return email;
        });
    }

    private Function<Map<String,Object>,Context> buildContext() {
        return ( m ->  new Context(Locale.getDefault(),m));
    }

    private Function<Candidate,Map<String,Object>> baseDataBuilder( ) {
        return(candidate -> {
            Map<String,Object> map = new HashMap<>();
            map.put("candidateName",candidate.getFirstName()+" "+ candidate.getLastName());
            map.put("experience",candidate.getExperience());
            map.put("technologyStack",candidate.getTechnologyStack());
            map.put("interviewDate",candidate.getInterviewDate());
            return map;
        });
    }

    private Function<Map<String,Object>,Map<String,Object>> publishDataBuilder( ) {
        return( m -> {
                 Map<String,Object> map = new HashMap<>(m);
                 map.put("recruiterName","Recruitment Team");
                return map;
        } );
    }

    private Function<Map<String,Object>,Map<String,Object>> acknowledgedDataBuilder( ) {
        return( m -> {
            Map<String,Object> map = new HashMap<>(m);
            map.put("interviewerName","interviewerName");
            map.put("interviewerEmail","interviewerEmail");
            return map;
        } );
    }
}