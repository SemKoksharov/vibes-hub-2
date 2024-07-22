package dev.semkoksharov.vibeshub2.service.interfaces;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AudioStreamingService {

    void streamAudio(Long songID, HttpServletRequest request, HttpServletResponse response);
}
