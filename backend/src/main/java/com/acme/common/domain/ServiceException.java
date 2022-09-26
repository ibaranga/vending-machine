package com.acme.common.domain;

/**
 * Domain-service layer exceptions
 */
public abstract class ServiceException extends RuntimeException {
    public ServiceException() {
    }

    public ServiceException(String message) {
        super(message);
    }

    public static class EntityNotFoundException extends ServiceException {
        public EntityNotFoundException() {
        }

        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class PermissionDeniedException extends ServiceException {
        public PermissionDeniedException() {
        }

        public PermissionDeniedException(String message) {
            super(message);
        }
    }

    public static class Unauthorized extends ServiceException {
        public Unauthorized() {
        }

        public Unauthorized(String message) {
            super(message);
        }
    }

    public static class ConflictException extends ServiceException {
        public ConflictException() {
        }

        public ConflictException(String message) {
            super(message);
        }
    }

    public static class InvalidRequest extends ServiceException {
        public InvalidRequest() {
        }

        public InvalidRequest(String message) {
            super(message);
        }
    }

}
