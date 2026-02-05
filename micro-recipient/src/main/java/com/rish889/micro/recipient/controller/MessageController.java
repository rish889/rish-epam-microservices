package com.rish889.micro.recipient.controller;

import com.rish889.micro.recipient.service.MessageStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageStorage messageStorage;

    public MessageController(MessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    @GetMapping("/message")
    public ResponseEntity<String> getMessage() {
        logger.info("GET /message endpoint called");
        String message = messageStorage.getAndRemoveOneMessage();
        if (message != null) {
            logger.info("Returning 1 message from storage");
            return ResponseEntity.ok(message);
        }
        logger.info("No messages available");
        return ResponseEntity.noContent().build();
    }
}
