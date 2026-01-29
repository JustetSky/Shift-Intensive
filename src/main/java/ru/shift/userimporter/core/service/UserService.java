package ru.shift.userimporter.core.service;

import ru.shift.userimporter.core.model.UserFilter;
import java.util.List;
import ru.shift.userimporter.core.model.User;

public interface UserService {
    List<User> getUsers(UserFilter filter);
}