package com.softvision.controller;

import com.softvision.model.Login;
import com.softvision.service.LoginService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.concurrent.CompletableFuture;


@Path("/login")
public class LoginController {

    @Inject
    LoginService loginService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUsers(@Suspended AsyncResponse asyncResponse,
                              Login login) {

        asyncResponse.resume(loginService.register(login));
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void loginValidate(@Suspended AsyncResponse asyncResponse,
                              @QueryParam("name") String name, @QueryParam("pass") String pass) {

        CompletableFuture.supplyAsync(() -> loginService.login(name, pass))
                .thenApply(v -> asyncResponse.resume(v))
                .exceptionally(v -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(v.getMessage()).build()));

    }

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public void getAllUsers(@Suspended AsyncResponse asyncResponse) {

        CompletableFuture.supplyAsync(() -> loginService.getAll())
                .thenApply(v -> asyncResponse.resume(v))
                .exceptionally(v -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(v.getMessage()).build()));

    }
}
