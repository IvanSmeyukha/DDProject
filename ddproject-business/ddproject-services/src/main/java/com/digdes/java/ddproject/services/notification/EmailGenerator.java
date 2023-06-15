package com.digdes.java.ddproject.services.notification;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
public class EmailGenerator {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    @Value("${spring.mail.username}")
    private final String addressFrom;

    @SneakyThrows
    public MimeMessage generateEmail(String email, String memberName) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
        Context context = new Context();
        context.setVariable("memberName", memberName);
        message.setText(templateEngine.process("email-template", context), true);
        message.setFrom(addressFrom);
        message.setTo(email);
        message.setSubject("Новая задача");
        return mimeMessage;
    }
}
