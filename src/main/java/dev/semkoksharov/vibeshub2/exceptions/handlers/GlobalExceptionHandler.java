package dev.semkoksharov.vibeshub2.exceptions.handlers;

import dev.semkoksharov.vibeshub2.dto.forms.ErrorResponseForm;
import dev.semkoksharov.vibeshub2.exceptions.*;
import dev.semkoksharov.vibeshub2.utils.DateTimeUtil;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseForm> handleIllegalArgExceptions(IllegalArgumentException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponseForm> handleEntityNotFoundExceptions(EntityNotFoundException ex) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoFieldsHaveBeenUpdatedException.class)
    public ResponseEntity<ErrorResponseForm> handleNoFieldsUpdatedExceptions(NoFieldsHaveBeenUpdatedException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EmptyResultException.class)
    public ResponseEntity<ErrorResponseForm> handleEmptyResultExceptions(EmptyResultException ex) {
        return createErrorResponse(ex, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmptySongsListOnUpdateException.class)
    public ResponseEntity<ErrorResponseForm> handleEmptySongsListOnUpdateExceptions(EmptySongsListOnUpdateException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidUserFieldsException.class)
    public ResponseEntity<ErrorResponseForm> handleInvalidUserFieldsExceptions(InvalidUserFieldsException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NegativeIdException.class)
    public ResponseEntity<ErrorResponseForm> handleNegativeIdExceptions(NegativeIdException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnknownUserRoleException.class)
    public ResponseEntity<ErrorResponseForm> handleUnknownUserRoleExceptions(UnknownUserRoleException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UnsupportedFileFormatException.class)
    public ResponseEntity<ErrorResponseForm> handleUnsupportedFileFormatExceptions(UnsupportedFileFormatException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PlaylistUpdateException.class)
    public ResponseEntity<ErrorResponseForm> handlePlaylistUpdateExceptions(PlaylistUpdateException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MinIOServiceException.class)
    public ResponseEntity<ErrorResponseForm> handleMinIOFileUploadExceptions(MinIOServiceException ex) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EntityMappingException.class)
    public ResponseEntity<ErrorResponseForm> handleEntityMappingExceptions(EntityMappingException ex) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponseForm> handleMaxUploadSizeExceededExceptions(MaxUploadSizeExceededException ex) {
        return createErrorResponse(ex, HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(AudioStreamingFailedException.class)
    public ResponseEntity<ErrorResponseForm> handleAudioStreamingFailedExceptions(AudioStreamingFailedException ex) {
        return createErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseForm> handleAccessDeniedExceptions(AccessDeniedException ex) {
        return createErrorResponse(ex, HttpStatus.FORBIDDEN);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(FilesNotUploadedException.class)
    public ResponseEntity<ErrorResponseForm> handleMultiUploadFailedException(FilesNotUploadedException ex) {
        return createErrorResponse(ex, HttpStatus.BAD_REQUEST);

    }

    private ResponseEntity<ErrorResponseForm> createErrorResponse(Exception ex, HttpStatus status) {

        String exception = ex.getClass().toString();
        String statusString = status.toString();
        String message = ex.getMessage();
        String timestamp = DateTimeUtil.getFormattedTimestamp();
        String stackTrace = Arrays.toString(ex.getStackTrace());

        ErrorResponseForm errorResponseEntity = new ErrorResponseForm(exception, statusString, message, timestamp, stackTrace);

        return ResponseEntity.status(status).body(errorResponseEntity);
    }
}
