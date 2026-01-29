package ru.shift.userimporter.core.service;

import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.model.UploadedFile;

import java.util.List;

public interface FileStatisticService {
    UploadedFile getFileById(Long fileId) throws ServiceException;
    List<UploadedFile> getFilesByStatus(FileStatus status);
}
