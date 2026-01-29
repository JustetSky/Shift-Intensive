package ru.shift.userimporter.core.service;

import ru.shift.userimporter.api.dto.ProcessingError;
import ru.shift.userimporter.core.exception.ServiceException;

import java.util.List;

public interface FileProcessingService {
    void processFile(Long fileId) throws ServiceException;
    List<ProcessingError> getProcessingErrors(Long fileId);
}