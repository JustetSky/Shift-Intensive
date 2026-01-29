package ru.shift.userimporter.core.util;

import ru.shift.userimporter.api.dto.ProcessingError;
import ru.shift.userimporter.api.dto.UserCsvRow;
import ru.shift.userimporter.core.exception.ServiceException;
import ru.shift.userimporter.core.model.ProcessingErrorCode;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class CsvParser {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ParseResult parseCsvLine(int lineNumber, String line) {
        List<UserCsvRow> validRows = List.of();
        List<ProcessingError> errors = List.of();

        try {
            String[] parts = line.split(",", -1);
            if (parts.length != 6) {
                return new ParseResult(List.of(), List.of(
                        new ProcessingError((int) lineNumber,
                                ProcessingErrorCode.INVALID_FORMAT.name(),
                                "Некорректное количество полей в строке " + lineNumber + ". Ожидается 6 полей")));
            }

            String lastName = parts[0].trim();
            String firstName = parts[1].trim();
            String middleName = parts[2].trim().isEmpty() ? null : parts[2].trim();
            String email = parts[3].trim();
            String phone = parts[4].trim();
            String birthDateStr = parts[5].trim();

            LocalDate.parse(birthDateStr, DATE_FORMATTER);

            UserCsvRow row = new UserCsvRow(firstName, lastName, middleName, email, phone, birthDateStr);
            return new ParseResult(List.of(row), List.of());

        } catch (DateTimeParseException e) {
            return new ParseResult(List.of(), List.of(
                    new ProcessingError((int) lineNumber,
                            ProcessingErrorCode.INVALID_BIRTHDATE.name(),
                            "Некорректный формат даты в строке " + lineNumber + ": yyyy-MM-dd")));
        } catch (Exception e) {
            return new ParseResult(List.of(), List.of(
                    new ProcessingError((int) lineNumber,
                            ProcessingErrorCode.INVALID_FORMAT.name(),
                            "Ошибка парсинга строки " + lineNumber + ": " + e.getMessage())));
        }
    }

    public record ParseResult(List<UserCsvRow> validRows, List<ProcessingError> errors) {}
}
