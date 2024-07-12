package dev.semkoksharov.vibeshub2.dto.forms;

public record ErrorResponseForm(String exception, String status, String message, String timestamp, String stackTrace) {}
