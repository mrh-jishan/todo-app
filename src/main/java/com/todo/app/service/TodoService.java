package com.todo.app.service;

import com.todo.app.dto.TodoRequest;
import com.todo.app.dto.TodoResponse;

import java.util.List;

public interface TodoService {

    List<TodoResponse> getTodosForUser(String username);

    /** Ownership-agnostic lookup — any authenticated user can view a todo by ID. */
    TodoResponse findTodoById(Long id);

    TodoResponse getTodoById(String username, Long id);

    TodoResponse createTodo(String username, TodoRequest request);

    TodoResponse updateTodo(String username, Long id, TodoRequest request);

    void toggleTodo(String username, Long id);

    void deleteTodo(String username, Long id);
}
