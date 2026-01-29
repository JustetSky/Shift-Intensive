package ru.shift.userimporter.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.shift.userimporter.api.dto.UserResponse;
import ru.shift.userimporter.core.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "name", source = "firstName")
    @Mapping(target = "birthdate", source = "birthDate")
    @Mapping(target = "creationTime", source = "createdAt")
    @Mapping(target = "updateTime", source = "updatedAt")
    UserResponse toResponse(User user);
}