package com.softvision.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.softvision.constant.ServiceConstants;
import com.softvision.controller.EmailController;
import com.softvision.exception.ServiceException;
import com.softvision.model.Email;
import com.softvision.service.EmailService;

/**
 * The Class EmailServiceImpl.
 *
 * @author arun.p
 */
@Service
public class EmailServiceImpl implements EmailService {

	/** The java mail sender. */
	@Inject
	private JavaMailSender javaMailSender;

	/** The env. */
	@Inject
	private Environment env;

	/** The email template engine. */
	@Autowired
	private TemplateEngine emailTemplateEngine;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.softvision.service.EmailService#sendEmail(com.softvision.model.Email)
	 */
	@Override
	public String sendEmail(final Email email) throws ServiceException {

		try {
			MimeMessage mimeMessage = javaMailSender.createMimeMessage();

			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(env.getProperty(ServiceConstants.PROPERTY_FROM_ADDRESS));
			if (!StringUtils.isBlank(email.getToRecipients())) {
				String[] toRecipients = retrieveValidAddresses(email.getToRecipients());

				if (toRecipients != null && toRecipients.length > ServiceConstants.ZERO) {
					mimeMessageHelper.setTo(toRecipients);
				} else {
					throw new Exception(ServiceConstants.TO_RECIPIENTS_IS_INVALID);
				}
			} else {
				throw new Exception(ServiceConstants.TO_RECIPIENTS_IS_INVALID);
			}
			if (!StringUtils.isBlank(email.getCcRecipients())) {
				String[] cCRecipients = retrieveValidAddresses(email.getCcRecipients());

				if (cCRecipients.length > ServiceConstants.ZERO) {
					mimeMessageHelper.setCc(cCRecipients);
				} else {

					LOGGER.info((ServiceConstants.CC_RECIPIENTS_IS_INVALID));
				}
			} else {
				LOGGER.info((ServiceConstants.CC_RECIPIENTS_IS_INVALID));
			}
			if (!StringUtils.isBlank(email.getBccRecipients())) {
				String[] bCCRecipients = retrieveValidAddresses(email.getBccRecipients());

				if (bCCRecipients.length > ServiceConstants.ZERO) {
					mimeMessageHelper.setBcc(bCCRecipients);
				} else {
					LOGGER.info(ServiceConstants.BCC_RECIPIENTS_IS_INVALID);
				}
			} else {
				LOGGER.info(ServiceConstants.BCC_RECIPIENTS_IS_INVALID);
			}
			if (!StringUtils.isBlank(email.getReplyTo())) {
				mimeMessageHelper.setReplyTo(email.getReplyTo());
			}
			mimeMessageHelper.setSubject(email.getSubject());

			String mergedEmailBody = getMergedEmailBodyWithTemplate(email.getBody(), email.getTemplateName());

			mimeMessageHelper.setText(mergedEmailBody, true);
			javaMailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new ServiceException(
					ServiceConstants.IN + ServiceConstants.BLANK_SPACE + ServiceConstants.SEND_EMAIL_METHOD
							+ ServiceConstants.DOUBLE_COLON + ServiceConstants.EXCEPTION_IN_SENDING_MAIL
							+ ServiceConstants.DOUBLE_COLON + e.getLocalizedMessage());
		}

		return ServiceConstants.MAIL_SENT;
	}

	/**
	 * Gets the merged email body with template.
	 *
	 * @param body
	 *            the body
	 * @param templateName
	 * @return the merged email body with template
	 */
	private String getMergedEmailBodyWithTemplate(final JSONObject body, String templateName) {

		Context contextForBody = new Context();
		String mergedBody = null;
		try {
			contextForBody = getDataForTemplateMerge(body.toJSONString());

			String templateBody = getTemplateContentFromFile(templateName);

			mergedBody = getMergedBodyWithTemplate(templateBody, contextForBody);

		} catch (Exception e) {

			if (e instanceof IOException) {
				LOGGER.error(ServiceConstants.FAILED_TO_GET_TEMPLATE_FILE, e);
				throw new ServiceException(ServiceConstants.FAILED_TO_GET_TEMPLATE_FILE);
			}
			LOGGER.error(ServiceConstants.FAILED_TO_MERGE, e);
			throw new ServiceException(ServiceConstants.FAILED_TO_MERGE);
		}
		return mergedBody;
	}

	/**
	 * Gets the template content from file.
	 * 
	 * @param templateName
	 *
	 * @return the template content from file
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private String getTemplateContentFromFile(String templateName) throws IOException {
		ClassLoader classLoader = getClass().getClassLoader();

		String templateContent = IOUtils.toString(classLoader.getResourceAsStream(ServiceConstants.TEMPLATES_FOLDER
				+ ServiceConstants.BACK_SLASH + templateName + ServiceConstants.DOT + ServiceConstants.HTML));

		return templateContent;
	}

	/**
	 * Retrieve valid addresses.
	 *
	 * @param recipientsMailAddresses
	 *            the recipients mail addresses
	 * @return the list
	 */
	private String[] retrieveValidAddresses(final String recipientsMailAddresses) {
		String[] recipientsAddresses = recipientsMailAddresses.split(ServiceConstants.COMMA);
		Stream<String> validRecipientsAddresses = Stream.of(recipientsAddresses).filter(x -> !StringUtils.isBlank(x));
		return validRecipientsAddresses.toArray(String[]::new);

	}

	/**
	 * To get data for template merge.
	 *
	 * @param body
	 *            the body
	 * @return the data for template merge
	 * @throws Exception
	 *             the exception
	 */
	@SuppressWarnings("unchecked")
	private Context getDataForTemplateMerge(final String body) throws Exception {
		Context context = new Context();
		JSONParser jsonParser = new JSONParser();

		JSONObject jsonObject = (JSONObject) jsonParser.parse(body);
		jsonParser.parse(body);
		jsonObject.keySet().forEach(key -> {
			if (jsonObject.get(key).toString().contains(ServiceConstants.OPNING_SQUARE_BRACKET)) {
				List<String> dataList = new ArrayList<String>();
				JSONArray jsonArray = new JSONArray();
				try {
					jsonArray = (JSONArray) jsonParser.parse(jsonObject.get(key).toString());
					for (int i = 0; i < jsonArray.size(); i++) {
						dataList.add(jsonArray.get(i).toString());
					}
				} catch (ParseException e) {
					LOGGER.error(ServiceConstants.JSON_ARRAY_PARSING_FAILED);
				}

				context.setVariable((String) key, dataList);
			} else {
				context.setVariable((String) key, jsonObject.get(key));
			}
		});
		return context;
	}

	/**
	 * merge the mail body with template.
	 *
	 * @param templateBody
	 *            the template body
	 * @param context
	 *            the context
	 * @return the merged body with template
	 * @throws Exception
	 *             the exception
	 */
	private String getMergedBodyWithTemplate(final String templateBody, final Context context) throws Exception {
		try {

			return emailTemplateEngine.process(templateBody, context);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}
}
