package com.rish889.micro.recipient.scheduler;

import com.rish889.micro.recipient.service.MessageStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MessageScheduler {

    private static final Logger logger = LoggerFactory.getLogger(MessageScheduler.class);

    private final MessageStorage messageStorage;

    public MessageScheduler(MessageStorage messageStorage) {
        this.messageStorage = messageStorage;
    }

    @Scheduled(fixedRateString = "${scheduler.interval.seconds}000")
    public void logMessageStatus() {
        int messageCount = messageStorage.getMessageCount();
        logger.info("Scheduler running - Current messages in storage: {}", messageCount);
    }
}
