package com.rish889.micro.collector.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "micro-recipient", url = "${micro-recipient.url}")
public interface MicroRecipientClient {

    @GetMapping("/message")
    ResponseEntity<String> getMessage();
}
