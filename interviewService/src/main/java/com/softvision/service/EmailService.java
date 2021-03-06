package com.softvision.service;

import com.softvision.exception.ServiceException;
import com.softvision.model.Email;
import com.softvision.model.Employee;

import java.util.Collection;

/**
 * The Interface EmailService.
 *
 * @author arun.p
 */
public interface EmailService {

	/**
	 * Send email.
	 *
	 * @param email the email
	 * @return the string
	 * @throws ServiceException the service exception
	 */
	String sendEmail(final Email email) throws ServiceException;

}
