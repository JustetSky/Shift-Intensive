package ru.shift.userimporter.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.FileErrorCode;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.service.FileStatisticService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileStatisticServiceImpl implements FileStatisticService {
    private final UploadedFileRepository uploadedFileRepository;

    @Override
    public UploadedFile getFileById(Long id) throws ServiceException {
        return uploadedFileRepository.findById(id)
                .orElseThrow(() -> new ServiceException(FileErrorCode.NOT_FILE_WITH_ID,
                        "File not found with id: " + id, null));
    }

    @Override
    public List<UploadedFile> getFilesByStatus(FileStatus status) {
        if (status == null) {
            return uploadedFileRepository.findAll();
        }
        return uploadedFileRepository.findUploadedFileByFileStatus(status);
    }
}
