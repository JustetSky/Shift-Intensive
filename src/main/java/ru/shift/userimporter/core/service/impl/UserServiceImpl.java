package ru.shift.userimporter.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.shift.userimporter.core.model.User;
import ru.shift.userimporter.core.model.UserFilter;
import ru.shift.userimporter.core.repository.UserRepository;
import ru.shift.userimporter.core.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<User> getUsers(UserFilter filter) {
        return userRepository.findUserWithFilters(
                filter.getPhone(),
                filter.getName(),
                filter.getLastName(),
                filter.getEmail(),
                filter.getLimit(),
                filter.getOffset()
        );
    }
}