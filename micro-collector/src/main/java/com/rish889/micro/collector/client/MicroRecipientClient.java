package com.rish889.micro.collector.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "micro-recipient", url = "${micro-recipient.url}")
public interface MicroRecipientClient {

    @GetMapping("/message")
    List<String> getMessages();
}
