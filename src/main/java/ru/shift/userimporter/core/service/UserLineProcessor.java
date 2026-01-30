package ru.shift.userimporter.core.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shift.userimporter.api.dto.ProcessingError;
import ru.shift.userimporter.api.dto.UserCsvRow;
import ru.shift.userimporter.core.model.ProcessingErrorCode;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.repository.UserRepository;
import ru.shift.userimporter.core.util.CsvParser;
import ru.shift.userimporter.core.util.UserValidator;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLineProcessor {

    private final UserValidator userValidator;
    private final UserRepository userRepository;

    public ProcessLineResult processLine(int lineNumber, String line) {
        try {
            var parseResult = CsvParser.parseCsvLine(lineNumber, line);

            if (!parseResult.errors().isEmpty()) {
                return new ProcessLineResult(false, null, parseResult.errors());
            }

            UserCsvRow row = parseResult.validRows().get(0);

            List<ProcessingError> validationErrors = userValidator.validate(lineNumber, row);
            if (!validationErrors.isEmpty()) {
                return new ProcessLineResult(false, null, validationErrors);
            }

            Optional<User> existingUser = userRepository.findUserByPhone(row.phone());
            User user = existingUser.orElse(User.builder()
                    .firstName(row.firstName())
                    .lastName(row.lastName())
                    .middleName(row.middleName())
                    .email(row.email())
                    .phone(row.phone())
                    .birthDate(LocalDate.parse(row.birthDate()))
                    .updatedAt(OffsetDateTime.now())
                    .createdAt(existingUser.isEmpty() ? OffsetDateTime.now() : null)
                    .build());

            userRepository.save(user);

            return new ProcessLineResult(existingUser.isPresent(), null, List.of());

        } catch (Exception e) {
            ProcessingError error = new ProcessingError(
                    lineNumber,
                    ProcessingErrorCode.INVALID_FORMAT.name(),
                    "Системная ошибка в строке " + lineNumber + ": " + e.getMessage()
            );
            return new ProcessLineResult(false, line, List.of(error));
        }

    }

    public record ProcessLineResult(
            boolean wasUpdated,
            String rawLine,
            List<ProcessingError> errors
    ) {
        public boolean isSuccess() { return errors.isEmpty(); }
        public boolean isInsert() { return isSuccess() && !wasUpdated; }
    }
}
