package com.todo.app.service;

import com.todo.app.dto.RegisterRequest;
import com.todo.app.entity.User;

public interface UserService {

    User register(RegisterRequest request);

    User findByUsername(String username);
}
