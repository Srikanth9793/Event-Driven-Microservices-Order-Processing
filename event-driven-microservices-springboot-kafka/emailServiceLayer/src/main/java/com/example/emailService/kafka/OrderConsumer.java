package com.example.emailService.kafka;

import com.example.baseDomains.dto.OrderEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class OrderConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderConsumer.class);

    @Autowired
    private JavaMailSender javaMailSender;

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(OrderEvent event) {

        LOGGER.info(String.format("Order event received in email-service => %s", event.toString()));

        // email the customer
        sendEmail(event);
    }

    public void sendEmail(OrderEvent event) {

        String fromEmail = "<sender_email>";
        String toEmail = "<receiver_email>";
        String subject = "Order Event";
        String text = String.format("Thank you for ordering: name: %s, quantity: %d, price: $%2f. Your order ID: %s", event.getOrder().getName(), event.getOrder().getQty(), event.getOrder().getPrice(), event.getOrder().getOrderId());

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

        System.out.println("Mail sent successfully...");

    }
}
