package com.org.ui.service;

public interface EmailService {
    void sendSignupConfirmationEmail(String toEmail, String userName);
}
