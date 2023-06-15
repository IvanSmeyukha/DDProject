package com.digdes.java.ddproject.services.notification.amqp;

import com.digdes.java.ddproject.services.notification.EmailGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.javamail.JavaMailSender;

@Slf4j
@RequiredArgsConstructor
public class EmailMessageConsumer {
    private final EmailGenerator emailGenerator;
    private final JavaMailSender mailSender;

    @RabbitListener(queues = "#{@queueName}")
    public void receiveMessage(Message message) {
        String email = message.getMessageProperties().getHeader("email");
        String memberName = message.getMessageProperties().getHeader("name");
        mailSender.send(emailGenerator.generateEmail(email, memberName));
        log.info("Notification sent to {}", email);
    }
}
