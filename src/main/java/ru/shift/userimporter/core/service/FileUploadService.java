package ru.shift.userimporter.core.service;

import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.model.UploadedFile;

import java.util.List;

public interface FileUploadService {
    Long uploadFile(MultipartFile file) throws ServiceException;
}