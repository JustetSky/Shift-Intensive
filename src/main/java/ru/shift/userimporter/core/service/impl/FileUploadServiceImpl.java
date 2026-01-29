package ru.shift.userimporter.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.FileErrorCode;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.model.UploadedFile;
import ru.shift.userimporter.core.repository.UploadedFileRepository;
import ru.shift.userimporter.core.service.FileUploadService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements FileUploadService {
    private final UploadedFileRepository uploadedFileRepository;
    private final Path storageLocation = Paths.get("./uploads");

    @Override
    @Transactional
    public Long uploadFile(MultipartFile file) throws ServiceException {
        if (file == null || isFileEmpty(file)) {
            throw new ServiceException(FileErrorCode.FILE_EMPTY, "File is empty", null);
        }

        String originalFilename = file.getOriginalFilename();

        byte[] fileBytes;
        try {
            fileBytes = file.getBytes();
        } catch (IOException e) {
            throw new ServiceException(FileErrorCode.FILE_STORAGE_ERROR, "Unable to read file content", e);
        }

        try {
            if (!Files.exists(storageLocation)) {
                Files.createDirectories(storageLocation);
            }

            String storageFilename = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = storageLocation.resolve(storageFilename);

            if (uploadedFileRepository.existsByStoragePath(filePath.toString())) {
                throw new ServiceException(FileErrorCode.DUPLICATE_FILE,
                        "File with this path already exists", null);
            }
            
            Files.copy(file.getInputStream(), filePath);
            

            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setOriginalFilename(originalFilename);
            uploadedFile.setStoragePath(filePath.toString());
            uploadedFile.setFileStatus(FileStatus.NEW);
            uploadedFile.setInsertedRows(0);
            uploadedFile.setUpdatedRows(0);

            return uploadedFileRepository.save(uploadedFile).getId();
        } catch (IOException e) {
            throw new ServiceException(FileErrorCode.FILE_STORAGE_ERROR,
                    "Failed to store file", e);
        }
    }
    
    private boolean isFileEmpty(MultipartFile file) {
        try {
            // Проверка размера файла
            if (file == null || file.isEmpty() || file.getSize() == 0) {
                return true;
            }

            // Проверка содержимого файла
            byte[] bytes = file.getBytes();
            if (bytes == null || bytes.length == 0) {
                return true;
            }

            // Проверка на файлы только с BOM или пробелами
            String content = new String(bytes, StandardCharsets.UTF_8)
                    .replace("\uFEFF", "")
                    .trim();

            return content.isEmpty();
        } catch (IOException e) {
            return true;
        }
    }
}