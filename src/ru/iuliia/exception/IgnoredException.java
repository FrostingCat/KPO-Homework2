package ru.iuliia.exception;

import ru.iuliia.model.MessageStorage;

public class IgnoredException extends HomeworkException {
    public IgnoredException(Throwable cause) { super(MessageStorage.OUTPUT_ERROR, cause); }
}
