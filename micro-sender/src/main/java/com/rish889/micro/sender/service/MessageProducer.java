package com.rish889.micro.sender.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    private final RabbitTemplate rabbitTemplate;
    private final String queueName;

    public MessageProducer(RabbitTemplate rabbitTemplate,
                          @Value("${rabbitmq.queue.name}") String queueName) {
        this.rabbitTemplate = rabbitTemplate;
        this.queueName = queueName;
    }

    public void sendMessage(String message) {
        logger.info("Sending message to queue '{}': {}", queueName, message);
        rabbitTemplate.convertAndSend(queueName, message);
        logger.info("Message sent successfully to queue '{}'", queueName);
    }
}
