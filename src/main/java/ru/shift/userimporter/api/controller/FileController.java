package ru.shift.userimporter.api.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
@RequestMapping(ApiPath.FILES)
@RequiredArgsConstructor
public class FileController {
    private final FileUploadService fileUploadService;
    private final FileProcessingService fileProcessingService;
    private final FileStatisticService fileStatisticService;
    private final FileMapper fileMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FileIdResponse uploadFile(@RequestParam("file") MultipartFile file) {
        Long fileId = fileUploadService.uploadFile(file);
        return new FileIdResponse(fileId);
    }

    @PostMapping(ApiPath.FILE_ID + ApiPath.PROCESSING)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void processFile(@PathVariable @Positive Long fileId) {
        fileProcessingService.processFile(fileId);
    }

    @GetMapping(ApiPath.STATISTICS)
    public List<FileResponse> getFilesStatistics(@RequestParam(required = false) FileStatus status) {
        return fileStatisticService.getFilesByStatus(status)
                .stream()
                .map(fileMapper::toFileResponse)
                .toList();
    }

    @GetMapping(ApiPath.FILE_ID + ApiPath.STATISTICS)
    public DetailedFileStatistic getDetailedStatistics( @PathVariable @Positive Long fileId) {
        return fileMapper.toDetailedFileStatistic(
                fileStatisticService.getFileById(fileId),
                fileProcessingService.getProcessingErrors(fileId)
        );
    }

}