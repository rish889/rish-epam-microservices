package com.rish889.micro.recipient.controller;

import com.rish889.micro.recipient.service.MessageStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MessageController {

    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private final MessageStorage messageStorage;

    public MessageController(MessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    @GetMapping("/message")
    public ResponseEntity<List<String>> getMessages() {
        logger.info("GET /message endpoint called");
        List<String> messages = messageStorage.getAndClearMessages();
        logger.info("Returning {} messages and clearing storage", messages.size());
        return ResponseEntity.ok(messages);
    }
}
