package ru.iuliia.exception;

import ru.iuliia.model.MessageStorage;

public class FileRelationException extends HomeworkException {
    public FileRelationException() { super(MessageStorage.ERROR_IN_FILE_RELATIONS); }
}
