package com.rish889.micro.visualizer.controller;

import com.rish889.micro.visualizer.entity.SavedMessage;
import com.rish889.micro.visualizer.service.VisualizerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VisualizerController {

    private final VisualizerService visualizerService;

    public VisualizerController(VisualizerService visualizerService) {
        this.visualizerService = visualizerService;
    }

    @GetMapping("/saved-messages")
    public ResponseEntity<List<SavedMessage>> getSavedMessages() {
        return ResponseEntity.ok(visualizerService.getSavedMessages());
    }
}
