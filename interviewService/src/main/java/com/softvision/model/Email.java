package com.softvision.model;

import java.io.Serializable;

import org.json.simple.JSONObject;

import lombok.Data;

/**
 * The Class Email.
 *
 * @author arun.p
 * Instantiates a new email.
 */
@Data
public class Email implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;


	/** Email From */
	private String from;

	/** The to recipients. */
	private String[] toRecipients;
	
	/** The cc recipients. */
	private String[] ccRecipients;
	
	/** The bcc recipients. */
	private String[] bccRecipients;
	
	/** The subject. */
	private String subject;
	
	/** The body. */
	private JSONObject body;
	
	/** The reply to. */
	private String replyTo;
	
	/** The template name. */
	private String templateName;

	/** The body. */
	private String text;




}
