package ru.shift.userimporter.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.model.UploadedFile;

import java.util.List;
import java.util.Optional;

public interface UploadedFileRepository extends JpaRepository<UploadedFile, Long> {
    boolean existsByStoragePath(String storagePath);
    
    Optional<UploadedFile> findUploadedFileById(Long id);

    List<UploadedFile> findUploadedFileByFileStatus(FileStatus status);
}