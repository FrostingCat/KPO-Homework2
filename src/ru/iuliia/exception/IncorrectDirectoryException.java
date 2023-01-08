package ru.iuliia.exception;

/**
 * Ошибка неправильной директории
 */
public class IncorrectDirectoryException extends HomeworkException {
    public IncorrectDirectoryException(String message, Throwable cause) { super(message, cause); }
}
