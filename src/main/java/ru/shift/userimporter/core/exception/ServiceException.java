package ru.shift.userimporter.core.exception;

import lombok.Getter;
import ru.shift.userimporter.core.model.FileErrorCode;

@Getter
public class ServiceException extends RuntimeException {
    private final FileErrorCode code;

    public ServiceException(FileErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public ServiceException(FileErrorCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}
