package com.rish889.micro.recipient.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageStorage {

    private final List<String> messages = Collections.synchronizedList(new ArrayList<>());

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> getAndClearMessages() {
        List<String> currentMessages = new ArrayList<>(messages);
        messages.clear();
        return currentMessages;
    }

    public String getAndRemoveOneMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.removeFirst();
    }

    public int getMessageCount() {
        return messages.size();
    }
}
