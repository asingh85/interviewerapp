package com.softvision.controller;

import com.softvision.common.ServiceConstants;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Path(ServiceConstants.BACK_SLASH + ServiceConstants.EMPLOYEE)
public class EmployeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmployeeController.class);

    @Inject
    EmployeeService employeeService;

    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
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
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.SEARCH)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void search(@Suspended AsyncResponse asyncResponse,
                       @QueryParam("str") String str) {
        LOGGER.info("Search string is  : {} ", str);
        if (StringUtils.isEmpty(str)) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(" Search string cannot be NULL or Empty.").build());

        }
        CompletableFuture.supplyAsync(() -> employeeService.search(str))
                .thenApply(list -> asyncResponse.resume(list))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.ALLEMPLOYEE)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllEmployees(@Suspended AsyncResponse asyncResponse,
                                @QueryParam("size") Integer size,
                                @QueryParam("sort") String sortOrder,
                                @QueryParam("isDeleted") boolean isDeleted) {

        LOGGER.info("Number of elements request is {} and sort order is {} and isDeleted {} ", size, sortOrder, isDeleted);
        if (StringUtils.isEmpty(sortOrder) && size < 1) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Number of elements request should not be 0 and sort order should be given").build());
        } else if (!isDeleted) {
            CompletableFuture.supplyAsync(() -> employeeService.getAllEmployees())
                    .thenApply(v -> (List<Employee>) v.get())
                    .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> !p.isDeleted()).collect(Collectors.toList())))
                    .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        } else {
            CompletableFuture.supplyAsync(() -> employeeService.getAllEmployees())
                    .thenApply(v -> (List<Employee>) v.get())
                    .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> p.isDeleted()).collect(Collectors.toList())))
                    .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
        }
    }

    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.RECRUITER)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllRecruiters(@Suspended AsyncResponse asyncResponse,
                                 @QueryParam("size") Integer size,
                                 @QueryParam("sort") String sortOrder,
                                 @QueryParam("isDeleted") boolean isDeleted) {

        LOGGER.info("Number of elements request is {} and sort order is {} and isDeleted {} ", size, sortOrder, isDeleted);
        if (StringUtils.isEmpty(sortOrder) && size < 1) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Number of elements request should not be 0 and sort order should be given").build());
        }
        CompletableFuture.supplyAsync(() -> employeeService.getAllEmployees())
                .thenApply(v -> (List<Employee>) v.get())
                .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> p.getEmployeeType().equals(EmployeeType.RECRUITER)).collect(Collectors.toList())))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
    }

    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.INTERVIEWER)
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllInterviwers(@Suspended AsyncResponse asyncResponse,
                                  @QueryParam("size") Integer size,
                                  @QueryParam("sort") String sortOrder,
                                  @QueryParam("isDeleted") boolean isDeleted) {

        LOGGER.info("Number of elements request is {} and sort order is {} and isDeleted {} ", size, sortOrder, isDeleted);
        if (StringUtils.isEmpty(sortOrder) && size < 1) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Number of elements request should not be 0 and sort order should be given").build());
        }
        CompletableFuture.supplyAsync(() -> employeeService.getAllEmployees())
                .thenApply(v -> (List<Employee>) v.get())
                .thenApply(k -> asyncResponse.resume(k.stream().sorted().filter(p -> p.getEmployeeType().equals(EmployeeType.INTERVIEWER)).collect(Collectors.toList())))
                .exceptionally(e -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
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

    @PUT
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
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
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.OPENING_CURLY_BRACKET + ServiceConstants.ID + ServiceConstants.CLOSING_CURLY_BRACKET)
    @Loggable
    public void deleteEmployee(@Suspended AsyncResponse asyncResponse,
                               @PathParam("id") String id) {

        LOGGER.info("Deleting candidate {} ", id);
        CompletableFuture future = CompletableFuture.runAsync(() -> employeeService.deleteEmployee(id));
        asyncResponse.resume(future.join());
    }

    @DELETE
    @Loggable
    public void deleteAllEmployees(@Suspended AsyncResponse asyncResponse) {
        LOGGER.info(" Deleting All candidates ");

        CompletableFuture future = CompletableFuture.runAsync(() -> employeeService.deleteAllEmployees());
        Response.ResponseBuilder rb = Response.ok("the test response");
        Response response = rb.header("Access-Control-Allow-Origin", "*")
                .status(Response.Status.BAD_REQUEST).entity("Input missing").build();
        asyncResponse.resume(response);
    }

    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.BYBANDEXP)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getAllEmployeesByBandExp(@Suspended AsyncResponse asyncResponse,
                                         @QueryParam("tc") String technologyCommunity,
                                         @QueryParam("be") int bandExperience) {

        if (StringUtils.isEmpty(technologyCommunity) && bandExperience < 3) {
            asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Technology Community should not be empty and the Band Experience should also not be empty").build());
        }
        LOGGER.info("Technology Community is {} and as per the Band Experience is {} ", technologyCommunity, bandExperience);
        CompletableFuture<Optional<List<Employee>>> future = CompletableFuture
                .supplyAsync(() -> employeeService.getAllEmployeesByBandExp(bandExperience, technologyCommunity));
        Optional<List<Employee>> employees = future.join();
        if (employees.isPresent()) {
            asyncResponse.resume(employees.get().stream()
                    .sorted(Comparator.comparing(Employee::getBandExperience))
                    .collect(Collectors.toList()));
        }
    }

    @GET
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.TECH)
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
    @Path(ServiceConstants.BACK_SLASH + ServiceConstants.EMPLOYEE_TYPE)
    @Produces(MediaType.APPLICATION_JSON)
    @Loggable
    public void getEmployeeByType(@Suspended AsyncResponse asyncResponse) {
        CompletableFuture<Optional<List<EmployeeType>>> future = CompletableFuture
                .supplyAsync(() -> employeeService.getEmployeeType());
        asyncResponse.resume(future.join().get().stream()
                .sorted()
                .collect(Collectors.toList()));
    }
}
