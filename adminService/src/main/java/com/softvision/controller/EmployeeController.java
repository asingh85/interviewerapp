package com.softvision.controller;

import com.softvision.helper.Loggable;
import com.softvision.model.Employee;
import com.softvision.model.EmployeeType;
import com.softvision.model.TechnologyCommunity;
import com.softvision.service.EmployeeService;
import com.softvision.validation.ValidationUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Path("/")
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Inject
    EmployeeService employeeService;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUsers(@Suspended AsyncResponse asyncResponse) {
        asyncResponse.resume(employeeService.register());
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void loginValidate(@Suspended AsyncResponse asyncResponse,
                              @QueryParam("email") String email, @QueryParam("pass") String pass) {

        CompletableFuture.supplyAsync(() -> employeeService.login(email, pass))
                .thenApply(v -> asyncResponse.resume(v))
                .exceptionally(v -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(v.getMessage()).build()));

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void addEmployee(@Suspended AsyncResponse asyncResponse,
                            Employee employee) {
        ValidationUtil.validate(employee);
        CompletableFuture.supplyAsync(() -> employeeService.addEmployee(employee))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()));
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getEmployeeById(@Suspended AsyncResponse asyncResponse,
                                @PathParam("id") String id) {
        LOGGER.info("Employee ID is : {} ", id);
        if (StringUtils.isEmpty(id)) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Employee Id  cannot be NULL or Empty.").build());

        }
        CompletableFuture.supplyAsync(() -> employeeService.getEmployeeById(id))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path("/recruiter/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void searchRecruiter(@Suspended AsyncResponse asyncResponse,
                                @QueryParam("str") String str) {
        LOGGER.info("Search string is  : {} ", str);
        if (StringUtils.isEmpty(str)) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Search string cannot be NULL or Empty.").build());

        }
        CompletableFuture.supplyAsync(() -> employeeService.searchRecruiter(str))
                .thenApply(list -> asyncResponse.resume(list))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path("/interviewer/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void searchInterviewer(@Suspended AsyncResponse asyncResponse,
                                  @QueryParam("str") String str) {
        LOGGER.info("Search string is  : {} ", str);
        if (StringUtils.isEmpty(str)) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Search string cannot be NULL or Empty.").build());

        }
        CompletableFuture.supplyAsync(() -> employeeService.searchInterviewer(str))
                .thenApply(list -> asyncResponse.resume(list))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }


    @GET
    @Path("/recruiter")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllRecruiters(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            return employeeService.getAllRecruiters();
        })
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path("/interviewer")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterviwers(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture.supplyAsync(() -> {
            return employeeService.getAllInterviewers();
        })
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));

    }


    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void updateEmployee(@Suspended AsyncResponse asyncResponse,
                               Employee employee, @PathParam("id") String id) {
        if (StringUtils.isEmpty(id)) {
            asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity("Input missing").build());
        }
        ValidationUtil.validate(employee);
        CompletableFuture.supplyAsync(() -> employeeService.updateEmployee(employee, id))
                .thenApply(optional -> asyncResponse.resume(optional.get()))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build()));

    }

    @DELETE
    @Path("/{id}")
    @Loggable
    public void deleteEmployee(@Suspended AsyncResponse asyncResponse,
                               @PathParam("id") String id) {

        LOGGER.info("Deleting employee {} ", id);
        CompletableFuture future = CompletableFuture.runAsync(() -> employeeService.deleteEmployee(id));
        asyncResponse.resume(future.join());
    }

    @DELETE
    @Loggable
    public void deleteAllEmployees(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All employee ");
        CompletableFuture future = CompletableFuture.runAsync(() -> employeeService.deleteAllEmployees());
        asyncResponse.resume(future.join());
    }

    @GET
    @Path("/interviewer/bybandexp")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllEmployeesByBandExp(@Suspended AsyncResponse asyncResponse,
                                         @QueryParam("be") int bandExperience,
                                         @QueryParam("tc") String technologyCommunity) {

        if (StringUtils.isEmpty(technologyCommunity) && bandExperience < 3) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Technology Community should not be empty and the Band Experience should also not be empty").build());
        }
        LOGGER.info("Technology Community is {} and as per the Band Experience is {} ", technologyCommunity, bandExperience);
        CompletableFuture<Optional<List<EmployeeType>>> future = CompletableFuture
                .supplyAsync(() -> employeeService.getAllEmployeesByBandExp(bandExperience, technologyCommunity));
        asyncResponse.resume(future.join().get().stream()
                .sorted()
                .collect(Collectors.toList()));
    }


    @GET
    @Path("/interviewer/tech")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getTechStack(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture<Optional<List<TechnologyCommunity>>> future = CompletableFuture
                .supplyAsync(() -> employeeService.getTechStack());
        asyncResponse.resume(future.join().get().stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/interviewer/employeetype")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getEmployeeByType(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture<Optional<List<EmployeeType>>> future = CompletableFuture
                .supplyAsync(() -> employeeService.getEmployeeType());
        asyncResponse.resume(future.join().get().stream()
                .sorted()
                .collect(Collectors.toList()));
    }

    @GET
    @Path("/interviewer/interviewertype")
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getInterviewerByType(@Suspended AsyncResponse asyncResponse,
                                    @QueryParam("tc") String technologyCommunity,
                                    @QueryParam("it") String interviewerType) {
        CompletableFuture<Optional<List<EmployeeType>>> future = CompletableFuture
                .supplyAsync(() -> employeeService.getInterviewerByType(technologyCommunity, interviewerType));
        asyncResponse.resume(future.join().get().stream()
                .sorted()
                .collect(Collectors.toList()));
    }


}
