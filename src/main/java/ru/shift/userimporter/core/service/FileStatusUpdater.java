package ru.shift.userimporter.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.FileErrorCode;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.repository.UploadedFileRepository;

@Service
@RequiredArgsConstructor
public class FileStatusUpdater {
    private final UploadedFileRepository uploadedFileRepository;

    @Transactional
    public UploadedFile setStatus(Long fileId, FileStatus status) throws ServiceException {
        UploadedFile file = uploadedFileRepository.findById(fileId)
                .orElseThrow(() -> new ServiceException(FileErrorCode.NOT_FILE_WITH_ID,
                        "File not found", null));

        file.setFileStatus(status);
        return uploadedFileRepository.save(file);
    }

    @Transactional
    public void updateWithStatistics(Long fileId, FileStatus status,
                                     int insertedRows, int updatedRows)
            throws ServiceException {
        UploadedFile file = setStatus(fileId, status);
        file.setInsertedRows(insertedRows);
        file.setUpdatedRows(updatedRows);
        uploadedFileRepository.save(file);
    }
}