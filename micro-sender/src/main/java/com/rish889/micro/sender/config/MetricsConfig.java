package com.rish889.micro.sender.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class MetricsConfig {

    private final AtomicInteger activeNotifications = new AtomicInteger(0);
    private final Random random = new Random();

    @Bean
    public Timer notificationTimer(MeterRegistry registry) {
        return Timer.builder("notification.send.time")
                .description("Time taken to send a notification")
                .tag("service", "micro-sender")
                .register(registry);
    }

    @Bean
    public Counter notificationsSentCounter(MeterRegistry registry) {
        return Counter.builder("notifications.sent.total")
                .description("Total number of notifications sent to RabbitMQ")
                .tag("service", "micro-sender")
                .register(registry);
    }

    @Bean
    public Counter notificationsFailedCounter(MeterRegistry registry) {
        return Counter.builder("notifications.failed.total")
                .description("Total number of failed notification attempts")
                .tag("service", "micro-sender")
                .register(registry);
    }

    @Bean
    public AtomicInteger activeNotificationsGauge(MeterRegistry registry) {
        Gauge.builder("notifications.active", activeNotifications, AtomicInteger::get)
                .description("Number of notifications currently being processed")
                .tag("service", "micro-sender")
                .register(registry);
        return activeNotifications;
    }

    @Bean
    public Gauge randomMetricGauge(MeterRegistry registry) {
        return Gauge.builder("system.random.value", random, Random::nextDouble)
                .description("A random value for demonstration purposes")
                .tag("service", "micro-sender")
                .tag("type", "random")
                .register(registry);
    }
}
