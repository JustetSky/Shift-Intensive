package ru.shift.userimporter.api.dto;

public record UserCsvRow(
        String firstName,
        String lastName,
        String middleName,
        String email,
        String phone,
        String birthDate
) {}
