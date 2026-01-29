package ru.shift.userimporter.api.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.shift.userimporter.api.ApiPath;
import ru.shift.userimporter.api.dto.DetailedFileStatistic;
import ru.shift.userimporter.api.dto.FileIdResponse;
import ru.shift.userimporter.api.dto.FileResponse;
import ru.shift.userimporter.api.mapper.FileMapper;
import ru.shift.userimporter.core.model.FileStatus;
import ru.shift.userimporter.core.service.FileStatisticService;
import ru.shift.userimporter.core.service.FileUploadService;
import ru.shift.userimporter.core.service.FileProcessingService;

import java.util.List;

@RestController
@RequestMapping(ApiPath.files)
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService fileUploadService;
    private final FileProcessingService fileProcessingService;
    private final FileStatisticService fileStatisticService;
    private final FileMapper fileMapper;

    @PostMapping
    public ResponseEntity<FileIdResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        Long fileId = fileUploadService.uploadFile(file);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new FileIdResponse(fileId));
    }

    @PostMapping(ApiPath.fileId + ApiPath.processing)
    public ResponseEntity<Void> processFile(@PathVariable @Positive Long fileId) {
        fileProcessingService.processFile(fileId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(ApiPath.statistics)
    public ResponseEntity<List<FileResponse>> getFilesStatistics(
            @RequestParam(required = false) FileStatus status) {
        List<FileResponse> statistics = fileStatisticService.getFilesByStatus(status)
                .stream()
                .map(fileMapper::toFileResponse)
                .toList();
        return ResponseEntity.ok(statistics);
    }

    @GetMapping(ApiPath.fileId + ApiPath.statistics)
    public ResponseEntity<DetailedFileStatistic> getDetailedStatistics(
            @PathVariable @Positive Long fileId) {
        DetailedFileStatistic statistic = fileMapper.toDetailedFileStatistic(
                fileStatisticService.getFileById(fileId),
                fileProcessingService.getProcessingErrors(fileId)
        );
        return ResponseEntity.ok(statistic);
    }

}