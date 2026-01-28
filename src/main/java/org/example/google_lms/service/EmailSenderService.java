package org.example.google_lms.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderService {

    //@Value("${spring.mail.enable}")
    private Boolean isEnable = true;
    @Qualifier("sender")
    private final JavaMailSender mailSender;
    private final RedisTemplate<String, Long> redisTemplate;

    public void sendOtpCode(String toEmail, Long code) {

        redisTemplate.opsForValue().set(toEmail, code, 3, TimeUnit.MINUTES);

        if (!isEnable) {
            log.info("Email sending is disabled. To: {},Code: {}", toEmail, code);
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject("Verification Code");
            String text = "Your verification code is: " + code + "\n" +
                    "This code is valid for 3 minutes.\n" +
                    "If you didn't request this code, please ignore this email.";
            message.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toEmail, e.getMessage());
        }
    }
}
