package com.rish889.micro.sender.controller;

import com.rish889.micro.sender.dto.NotificationRequest;
import com.rish889.micro.sender.service.MessageProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final MessageProducer messageProducer;

    public NotificationController(MessageProducer messageProducer) {
        this.messageProducer = messageProducer;
    }

    @PostMapping("/notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        logger.info("Received notification request for user: {}", request.user());
        messageProducer.sendMessage(request.message());
        return ResponseEntity.ok("Notification sent successfully");
    }
}
