package ru.shift.userimporter.api.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.FileErrorCode;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleFileException(ServiceException e) {
        ErrorResponse error = new ErrorResponse(e.getCode().name(), e.getMessage());

        HttpStatus status = e.getCode() == FileErrorCode.DUPLICATE_FILE
                ? HttpStatus.CONFLICT
                : HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(error);
    }
}
