package ru.shift.userimporter.api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.shift.userimporter.api.ApiPath;
import ru.shift.userimporter.api.dto.UserResponse;
import ru.shift.userimporter.api.mapper.UserMapper;
import ru.shift.userimporter.core.model.UserFilter;
import ru.shift.userimporter.core.service.UserService;

import java.util.List;

@Validated
@RestController
@RequestMapping(ApiPath.CLIENTS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponse> getUsers(@Valid UserFilter filter) {
        return userService.getUsers(filter)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
}