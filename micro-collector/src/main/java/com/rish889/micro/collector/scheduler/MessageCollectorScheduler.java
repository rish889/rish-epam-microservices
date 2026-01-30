package com.rish889.micro.collector.scheduler;

import com.rish889.micro.collector.client.MicroRecipientClient;
import com.rish889.micro.collector.entity.CollectedMessage;
import com.rish889.micro.collector.repository.CollectedMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessageCollectorScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MessageCollectorScheduler.class);

    private final MicroRecipientClient microRecipientClient;
    private final CollectedMessageRepository messageRepository;

    public MessageCollectorScheduler(MicroRecipientClient microRecipientClient,
                                     CollectedMessageRepository messageRepository) {
        this.microRecipientClient = microRecipientClient;
        this.messageRepository = messageRepository;
    }

    @Scheduled(fixedRateString = "${scheduler.interval.seconds}000")
    public void collectMessages() {
        logger.info("Scheduler triggered - Fetching messages from micro-recipient");
        try {
            List<String> messages = microRecipientClient.getMessages();
            logger.info("Collected {} messages from micro-recipient", messages.size());
            if (!messages.isEmpty()) {
                List<CollectedMessage> entities = messages.stream()
                        .map(CollectedMessage::new)
                        .toList();
                messageRepository.saveAll(entities);
                logger.info("Saved {} messages to database", entities.size());
            }
        } catch (Exception e) {
            logger.error("Failed to fetch messages from micro-recipient: {}", e.getMessage());
        }
    }
}
