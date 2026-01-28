package ru.shift.userimporter.core.service;

import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.core.exception.ServiceException;

public interface FileService {
    Long uploadFile(MultipartFile file) throws ServiceException;
}