package dev.semkoksharov.vibeshub2.service.interfaces;

import jakarta.servlet.http.HttpServletResponse;

public interface AudioStreamingServiceInt {
    void streamAudio(Long songID, HttpServletResponse response);
}
