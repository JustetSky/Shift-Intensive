package ru.shift.userimporter.core.util;

import org.springframework.stereotype.Component;
import ru.shift.userimporter.api.dto.ProcessingError;
import ru.shift.userimporter.api.dto.UserCsvRow;
import ru.shift.userimporter.core.model.ProcessingErrorCode;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Component
public class UserValidator {
    private static final Pattern NAME_PATTERN = Pattern.compile("^[А-ЯЁ][а-яёА-ЯЁ'\\- ]{2,50}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9._%-]+@(shift\\.com|shift\\.ru)$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^7\\d{10}$");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<ProcessingError> validate(int lineNumber, UserCsvRow row) {
        List<ProcessingError> errors = new ArrayList<>();

        validateName(lineNumber, row.lastName(), "Фамилия", errors);
        validateName(lineNumber, row.firstName(), "Имя", errors);
        if (row.middleName() != null && !row.middleName().isEmpty()) {
            validateName(lineNumber, row.middleName(), "Отчество", errors);
        }
        validateEmail(lineNumber, row.email(), errors);
        validatePhone(lineNumber, row.phone(), errors);
        validateBirthDate(lineNumber, row.birthDate(), errors);

        return errors;
    }

    private void validateName(int lineNumber, String value, String fieldName, List<ProcessingError> errors) {
        if (value == null || value.isEmpty() || !NAME_PATTERN.matcher(value).matches()) {
            errors.add(new ProcessingError(lineNumber,
                    getErrorCodeForField(fieldName).name(),
                    String.format("Строка %d: Некорректный формат %s", lineNumber, fieldName)));
        }
    }

    private ProcessingErrorCode getErrorCodeForField(String fieldName) {
        return switch (fieldName) {
            case "Имя" -> ProcessingErrorCode.INVALID_NAME;
            case "Фамилия" -> ProcessingErrorCode.INVALID_LAST_NAME;
            case "Отчество" -> ProcessingErrorCode.INVALID_MIDDLE_NAME;
            default -> ProcessingErrorCode.INVALID_FORMAT;
        };
    }

    private void validateEmail(int lineNumber, String email, List<ProcessingError> errors) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            errors.add(new ProcessingError(lineNumber,
                    ProcessingErrorCode.INVALID_EMAIL.name(),
                    "Строка " + lineNumber + ": Некорректный email. Только shift.com/ru"));
        }
    }

    private void validatePhone(int lineNumber, String phone, List<ProcessingError> errors) {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            errors.add(new ProcessingError(lineNumber,
                    ProcessingErrorCode.INVALID_PHONE.name(),
                    "Строка " + lineNumber + ": Телефон: 7 + 10 цифр"));
        }
    }

    private void validateBirthDate(int lineNumber, String birthDateStr, List<ProcessingError> errors) {
        if (birthDateStr == null || birthDateStr.isEmpty()) {
            errors.add(new ProcessingError(lineNumber,
                    ProcessingErrorCode.INVALID_BIRTHDATE.name(),
                    "Строка " + lineNumber + ": Дата рождения обязательна"));
            return;
        }
        try {
            LocalDate birthDate = LocalDate.parse(birthDateStr, DATE_FORMATTER);
            if (Period.between(birthDate, LocalDate.now()).getYears() < 18) {
                errors.add(new ProcessingError(lineNumber,
                        ProcessingErrorCode.INVALID_BIRTHDATE.name(),
                        "Строка " + lineNumber + ": Старше 18 лет"));
            }
        } catch (Exception e) {
            errors.add(new ProcessingError(lineNumber,
                    ProcessingErrorCode.INVALID_BIRTHDATE.name(),
                    "Строка " + lineNumber + ": yyyy-MM-dd"));
        }
    }
}
