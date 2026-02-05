package com.rish889.micro.sender.controller;

import com.rish889.micro.sender.dto.NotificationRequest;
import com.rish889.micro.sender.service.MessageProducer;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
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
    private final Timer notificationTimer;

    public NotificationController(MessageProducer messageProducer,
                                  Counter notificationsSentCounter,
                                  Counter notificationsFailedCounter,
                                  AtomicInteger activeNotificationsGauge,
                                  Timer notificationTimer) {
        this.messageProducer = messageProducer;
        this.notificationsSentCounter = notificationsSentCounter;
        this.notificationsFailedCounter = notificationsFailedCounter;
        this.activeNotifications = activeNotificationsGauge;
        this.notificationTimer = notificationTimer;
    }

    @PostMapping("/notification")
    public ResponseEntity<String> sendNotification(@RequestBody NotificationRequest request) {
        return notificationTimer.record(() -> {
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
        });
    }
}
