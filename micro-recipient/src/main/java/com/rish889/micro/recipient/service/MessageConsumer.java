package com.rish889.micro.recipient.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    private static final Logger logger = LoggerFactory.getLogger(MessageConsumer.class);

    private final MessageStorage messageStorage;

    public MessageConsumer(MessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void receiveMessage(String message) {
        logger.info("Received message from queue: {}", message);
        messageStorage.addMessage(message);
        logger.info("Message added to storage. Current count: {}", messageStorage.getMessageCount());
    }
}
