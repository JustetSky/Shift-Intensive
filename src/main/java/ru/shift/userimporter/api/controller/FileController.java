package ru.shift.userimporter.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.api.dto.FileIdResponse;
import ru.shift.userimporter.core.service.FileUploadService;
import ru.shift.userimporter.core.service.FileProcessingService;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService fileUploadService;
    private final FileProcessingService fileProcessingService;

    @PostMapping
    public ResponseEntity<FileIdResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        Long fileId = fileUploadService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FileIdResponse(fileId));
    }

    @PostMapping("/{fileId}/processing")
    public ResponseEntity<Void> processFile(@PathVariable Long fileId) {
        fileProcessingService.processFile(fileId);
        return ResponseEntity.noContent().build();
    }

}