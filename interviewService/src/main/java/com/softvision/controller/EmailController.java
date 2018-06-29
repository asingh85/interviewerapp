
package com.softvision.controller;

import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.softvision.constant.ServiceConstants;
import com.softvision.exception.ServiceException;
import com.softvision.model.Email;
import com.softvision.service.EmailService;

/**
 * The Class EmailController.
 *
 * @author arun.p
 */
@Path(ServiceConstants.BACK_SLASH + ServiceConstants.INTERVIEW_SERVICE)
public class EmailController {

	/** The email service. */
	@Inject
	EmailService emailService;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	/**
	 * Send email.
	 *
	 * @param asyncResponse the async response
	 * @param email the email
	 */
	@POST
	@Path(ServiceConstants.BACK_SLASH + ServiceConstants.SEND_EMAIL)
	@Consumes(MediaType.APPLICATION_JSON)
	public void sendEmail(@Suspended final AsyncResponse asyncResponse, final Email email) throws ServiceException{
		LOGGER.info(ServiceConstants.IN + ServiceConstants.BLANK_SPACE + ServiceConstants.SEND_EMAIL_METHOD
				+ ServiceConstants.DOUBLE_COLON + ServiceConstants.SENDING_EMAIL_TO + ServiceConstants.DOUBLE_COLON
				+ email.getToRecipients());
		CompletableFuture.supplyAsync(() -> emailService.sendEmail(email)).thenApply(
				emailSent -> asyncResponse.resume(Response.status(Response.Status.OK).entity(emailSent).build())).exceptionally(e -> asyncResponse.resume(
                        Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build()));
	}

}

