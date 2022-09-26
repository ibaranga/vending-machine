package com.acme.common.api;

import com.acme.common.domain.ServiceException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class AcmeControllerAdvice extends ResponseEntityExceptionHandler {
    private static final Map<Class<? extends ServiceException>, HttpStatus> HTTP_STATUS_MAP = Map.of(
            ServiceException.Unauthorized.class, HttpStatus.UNAUTHORIZED,
            ServiceException.EntityNotFoundException.class, HttpStatus.NOT_FOUND,
            ServiceException.PermissionDeniedException.class, HttpStatus.FORBIDDEN,
            ServiceException.ConflictException.class, HttpStatus.CONFLICT,
            ServiceException.InvalidRequest.class, HttpStatus.BAD_REQUEST
    );

    @ExceptionHandler(ServiceException.class)
    protected ResponseEntity<Object> handleException(ServiceException exception, WebRequest webRequest) {
        return handleExceptionInternal(
                exception,
                Map.of("status", "error", "message", Objects.requireNonNullElse(exception.getMessage(), "")),
                HttpHeaders.EMPTY,
                HTTP_STATUS_MAP.getOrDefault(exception.getClass(), HttpStatus.INTERNAL_SERVER_ERROR),
                webRequest);
    }
}
