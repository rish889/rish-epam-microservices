package com.rish889.micro.sender.controller;

import com.rish889.micro.sender.dto.NotificationRequest;
import com.rish889.micro.sender.service.MessageProducer;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class NotificationController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationController.class);

    private final MessageProducer messageProducer;
    private final Counter notificationsSentCounter;
    private final Counter notificationsFailedCounter;
    private final AtomicInteger activeNotifications;

    public NotificationController(MessageProducer messageProducer,
                                  Counter notificationsSentCounter,
                                  Counter notificationsFailedCounter,
                                  AtomicInteger activeNotificationsGauge) {
        this.messageProducer = messageProducer;
        this.notificationsSentCounter = notificationsSentCounter;
        this.notificationsFailedCounter = notificationsFailedCounter;
        this.activeNotifications = activeNotificationsGauge;
    }

    @PostMapping("/notification")
    @Timed(value = "notification.send.time", description = "Time taken to send a notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        logger.info("Received notification request for user: {}", request.user());
        activeNotifications.incrementAndGet();
        try {
            messageProducer.sendMessage(request.message());
            notificationsSentCounter.increment();
            return ResponseEntity.ok("Notification sent successfully");
        } catch (Exception e) {
            notificationsFailedCounter.increment();
            logger.error("Failed to send notification: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to send notification");
        } finally {
            activeNotifications.decrementAndGet();
        }
    }
}
