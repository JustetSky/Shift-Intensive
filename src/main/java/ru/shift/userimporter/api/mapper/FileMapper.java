package ru.shift.userimporter.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shift.userimporter.api.dto.*;
import ru.shift.userimporter.core.model.UploadedFile;
import java.util.List;

@Mapper(componentModel = "spring")
public interface FileMapper {

    @Mapping(target = "fileId", source = "id")
    @Mapping(target = "status", source = "fileStatus")
    @Mapping(target = "statistic.insertedLinesCount", source = "insertedRows", defaultValue = "0")
    @Mapping(target = "statistic.updatedLinesCount", source = "updatedRows", defaultValue = "0")
    FileResponse toFileResponse(UploadedFile file);

    default DetailedFileStatistic toDetailedFileStatistic(
            UploadedFile file,
            List<ProcessingError> errors
    ) {
        return new DetailedFileStatistic(
                file.getInsertedRows(),
                file.getUpdatedRows(),
                errors
        );
    }
}