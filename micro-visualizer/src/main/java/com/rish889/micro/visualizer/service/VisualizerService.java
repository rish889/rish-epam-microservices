package com.rish889.micro.visualizer.service;

import com.rish889.micro.visualizer.entity.SavedMessage;
import com.rish889.micro.visualizer.repository.SavedMessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VisualizerService {

    private final SavedMessageRepository savedMessageRepository;

    public VisualizerService(SavedMessageRepository savedMessageRepository) {
        this.savedMessageRepository = savedMessageRepository;
    }

    public List<SavedMessage> getSavedMessages() {
        return savedMessageRepository.findAll();
    }
}
