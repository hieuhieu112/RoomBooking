package com.app.backend.service.intf;

public interface EmailService {
    void sendEmail(
            String to,
            String subject,
            String htmlContent
    );
}
