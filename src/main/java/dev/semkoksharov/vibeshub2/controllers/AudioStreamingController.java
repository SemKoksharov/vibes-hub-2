package dev.semkoksharov.vibeshub2.controllers;

import dev.semkoksharov.vibeshub2.service.implementations.AudioStreamingService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stream")
public class AudioStreamingController {

    private final AudioStreamingService audioStreamingService;

    @Autowired
    public AudioStreamingController(AudioStreamingService audioStreamingService) {
        this.audioStreamingService = audioStreamingService;
    }

    @GetMapping(value = "/{songID}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public void streamAudio(@PathVariable Long songID, HttpServletResponse response) {
        audioStreamingService.streamAudio(songID, response);
    }
}
