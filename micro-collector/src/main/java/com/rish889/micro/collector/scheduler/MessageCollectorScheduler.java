package com.rish889.micro.collector.scheduler;

import com.rish889.micro.collector.client.MicroRecipientClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageCollectorScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MessageCollectorScheduler.class);

    private final MicroRecipientClient microRecipientClient;

    public MessageCollectorScheduler(MicroRecipientClient microRecipientClient) {
        this.microRecipientClient = microRecipientClient;
    }

    @Scheduled(fixedRateString = "${scheduler.interval.seconds}000")
    public void collectMessages() {
        logger.info("Scheduler triggered - Fetching messages from micro-recipient");
        try {
            List<String> messages = microRecipientClient.getMessages();
            logger.info("Collected {} messages from micro-recipient", messages.size());
            if (!messages.isEmpty()) {
                logger.info("Messages: {}", messages);
            }
        } catch (Exception e) {
            logger.error("Failed to fetch messages from micro-recipient: {}", e.getMessage());
        }
    }
}
