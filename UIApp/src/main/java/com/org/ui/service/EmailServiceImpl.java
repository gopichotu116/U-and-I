package com.org.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendSignupConfirmationEmail(String toEmail, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Registraion successfully completed");
        message.setText("Dear " + userName +"\r\n"
        		+ "\r\n"
        		+ "Thank you for registering with us. We're delighted to welcome you to our community!\r\n"
        		+ "\r\n"
        		+ "Your account has been successfully created, and you are now ready to explore all the features and benefits our platform has to offer.\r\n"
        		+ "\r\n"
        		+ "Should you have any questions or require assistance, please don't hesitate to contact our customer support team at [support email] or [phone number].\r\n"
        		+ "\r\n"
        		+ "Once again, welcome aboard! We're thrilled to have you with us.");

        emailSender.send(message);
    }
}
