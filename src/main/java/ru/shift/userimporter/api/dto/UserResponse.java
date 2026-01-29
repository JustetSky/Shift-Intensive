package ru.shift.userimporter.api.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Builder
public record UserResponse (
        Long phone,
        String name,
        String lastName,
        String middleName,
        String email,
        LocalDate birthdate,
        OffsetDateTime creationTime,
        OffsetDateTime updateTime
){
}