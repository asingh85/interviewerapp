package com.softvision.serviceimpl;

import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Stream;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import com.softvision.model.Employee;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
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

    /**
     * The java mail sender.
     */
    @Inject
    private JavaMailSender javaMailSender;

    /**
     * The env.
     */
    @Inject
    private Environment env;

    /**
     * The email template engine.
     */
    @Autowired
    private TemplateEngine emailTemplateEngine;

    /**
     * The Constant LOGGER.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EmailController.class);

    @Value("${email.service.from.address}")
    private String from;

    /*
     * (non-Javadoc)
     *
     * @see
     * com.softvision.service.EmailService#sendEmail(com.softvision.model.Email)
     */
    @Override
    public String sendEmail(final Email email) throws ServiceException {
        String status = null;
        try {
            String templateBody = getTemplateContentFromFile(email.getTemplateName());
            String mergedTemplate = mergeContentOnTemplate(templateBody, email.getContext());
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mailMessage = new MimeMessageHelper(mimeMessage, false, "utf-8");
            mimeMessage.setContent(mergedTemplate, "text/html");
            mailMessage.setFrom(from);
            mailMessage.setTo(email.getToRecipients());
            mailMessage.setSentDate(new Date());
            mailMessage.setSubject(email.getSubject());
            javaMailSender.send(mimeMessage);
            status = "Successfully send the mail";
        } catch (Exception ex) {
            status = "Exception while sending mail";
            ex.printStackTrace();
        }

        return status;
    }


    private String getTemplateContentFromFile(String templateName) throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        String templateContent = IOUtils.toString(classLoader.getResourceAsStream(ServiceConstants.TEMPLATES_FOLDER
                + ServiceConstants.BACK_SLASH + templateName + ServiceConstants.DOT + ServiceConstants.HTML));

        return templateContent;
    }

    private String mergeContentOnTemplate(final String templateBody, final Context context) throws Exception {
        try {
            return emailTemplateEngine.process(templateBody, context);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

}