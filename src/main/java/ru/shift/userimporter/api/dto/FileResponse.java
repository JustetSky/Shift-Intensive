package ru.shift.userimporter.api.dto;

import ru.shift.userimporter.core.model.FileStatus;

public record FileResponse(
        int fileId,
        FileStatus status,
        FileStatistic statistic
){}