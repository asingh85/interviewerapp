package com.softvision.serviceimpl;

import com.softvision.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

public class EmailServiceImpl implements EmailService {


    @Inject
    private JavaMailSender sender;


    @Override
    public void sendMail() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setTo("krishnakumar.arjunan@gmail.com");
        helper.setText("How are you?");
        helper.setSubject("Hi");
        sender.send(message);
    }
}
