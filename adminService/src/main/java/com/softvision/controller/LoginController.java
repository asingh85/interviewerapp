package com.softvision.controller;

import com.softvision.common.ServiceConstants;
import com.softvision.model.Login;
import com.softvision.service.LoginService;
import java.util.concurrent.CompletableFuture;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path(ServiceConstants.BACK_SLASH + ServiceConstants.LOGIN)
public class LoginController {

    @Inject
    LoginService loginService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUser(@Suspended AsyncResponse asyncResponse,
                             Login login) {

        asyncResponse.resume(loginService.register(login));
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public void registerUser(@Suspended AsyncResponse asyncResponse,
                             @QueryParam("name") String name, @QueryParam("pass") String pass) {

        CompletableFuture.supplyAsync(() -> loginService.login(name, pass))
                .thenApply(v -> asyncResponse.resume(v))
                .exceptionally(v -> asyncResponse.resume(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(v.getMessage()).build()));

    }
}
