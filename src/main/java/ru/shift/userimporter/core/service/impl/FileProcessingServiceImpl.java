package ru.shift.userimporter.core.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shift.userimporter.api.dto.ProcessingError;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.*;
import ru.shift.userimporter.core.repository.FileProcessingErrorRepository;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.service.FileProcessingService;
import ru.shift.userimporter.core.service.FileStatusUpdater;
import ru.shift.userimporter.core.service.UserLineProcessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileProcessingServiceImpl implements FileProcessingService {
    
    private final FileProcessingErrorRepository errorRepository;
    private final UserLineProcessor lineProcessor;
    private final FileStatusUpdater fileStatusUpdater;

    @Override
    @Async
    @Transactional
    public void processFile(Long fileId) throws ServiceException {
        UploadedFile file = fileStatusUpdater.setStatus(fileId, FileStatus.IN_PROGRESS);

        try {
            Path filePath = Path.of(file.getStoragePath());
            List<String> lines = Files.readAllLines(filePath);

            FileProcessingStatistics statistics = processLines(lines, file);

            fileStatusUpdater.updateWithStatistics(
                    fileId,
                    FileStatus.DONE,
                    statistics.insertedRows(),
                    statistics.updatedRows()
            );

        } catch (Exception e) {
            log.error("File processing failed for fileId: {}", fileId, e);
            fileStatusUpdater.setStatus(fileId, FileStatus.FAILED);
            throw new ServiceException(FileErrorCode.FILE_STORAGE_ERROR,
                    "File processing failed", e);
        }
    }

    private FileProcessingStatistics processLines(List<String> lines, UploadedFile file) {
        int insertedRows = 0;
        int updatedRows = 0;

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            UserLineProcessor.ProcessLineResult result = lineProcessor.processLine(i + 1, line);

            if (!result.isSuccess()) {
                for (ProcessingError dtoError : result.errors()) {
                    FileProcessingError entityError = new FileProcessingError();
                    entityError.setFile(file);
                    entityError.setRowNumber(dtoError.lineNumber());
                    entityError.setRawData(result.rawLine());

                    ProcessingErrorCode errorCodeEnum;
                    try {
                        errorCodeEnum = ProcessingErrorCode.valueOf(dtoError.errorCode());
                    } catch (IllegalArgumentException e) {
                        errorCodeEnum = ProcessingErrorCode.INVALID_FORMAT;
                    }
                    entityError.setErrorCode(errorCodeEnum);

                    entityError.setErrorMessage(dtoError.errorMessage());
                    errorRepository.save(entityError);
                }
            } else if (result.wasUpdated()) {
                updatedRows++;
            } else {
                insertedRows++;
            }
        }

        return new FileProcessingStatistics(insertedRows, updatedRows);
    }

    @Override
    public List<ProcessingError> getProcessingErrors(Long fileId) {
        return errorRepository.findProcessingErrorByFileId(fileId).stream()
                .map(entity -> new ProcessingError(
                        entity.getRowNumber(),
                        entity.getErrorCode().name(),
                        entity.getErrorMessage()
                ))
                .toList();
    }

    private record FileProcessingStatistics(int insertedRows, int updatedRows) {
    }
}
