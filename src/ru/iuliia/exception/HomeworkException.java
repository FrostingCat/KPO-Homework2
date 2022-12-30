package ru.iuliia.exception;

public class HomeworkException extends RuntimeException {
    public HomeworkException(String message, Throwable cause) {
        super(message, cause);
    }
    public HomeworkException(String message) {
        super(message);
    }
}
