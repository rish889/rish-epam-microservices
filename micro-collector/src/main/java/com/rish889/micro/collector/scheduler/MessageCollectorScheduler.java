package com.rish889.micro.collector.scheduler;

import com.rish889.micro.collector.client.MicroRecipientClient;
import com.rish889.micro.collector.entity.CollectedMessage;
import com.rish889.micro.collector.repository.CollectedMessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
    public void collectMessage() {
        logger.info("Scheduler triggered - Fetching one message from micro-recipient");
        try {
            ResponseEntity<String> response = microRecipientClient.getMessage();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                String message = response.getBody();
                logger.info("Collected 1 message from micro-recipient");
                CollectedMessage entity = new CollectedMessage(message);
                messageRepository.save(entity);
                logger.info("Saved 1 message to database");
            } else {
                logger.info("No messages available from micro-recipient");
            }
        } catch (Exception e) {
            logger.error("Failed to fetch message from micro-recipient: {}", e.getMessage());
        }
    }
}
