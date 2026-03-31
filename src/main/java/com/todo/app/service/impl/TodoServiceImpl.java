package com.todo.app.service.impl;

import com.todo.app.dto.TodoRequest;
import com.todo.app.dto.TodoResponse;
import com.todo.app.entity.Todo;
import com.todo.app.entity.User;
import com.todo.app.mapper.TodoMapper;
import com.todo.app.repository.TodoRepository;
import com.todo.app.repository.UserRepository;
import com.todo.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TodoServiceImpl implements TodoService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final TodoMapper todoMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TodoResponse> getTodosForUser(String username) {
        User user = resolveUser(username);
        return todoRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(todoMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public TodoResponse getTodoById(String username, Long id) {
        User user = resolveUser(username);
        return todoMapper.toResponse(resolveTodo(id, user));
    }

    @Override
    public TodoResponse createTodo(String username, TodoRequest request) {
        User user = resolveUser(username);
        Todo todo = Todo.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .user(user)
                .build();
        return todoMapper.toResponse(todoRepository.save(todo));
    }

    @Override
    public TodoResponse updateTodo(String username, Long id, TodoRequest request) {
        User user = resolveUser(username);
        Todo todo = resolveTodo(id, user);
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        return todoMapper.toResponse(todoRepository.save(todo));
    }

    @Override
    public void toggleTodo(String username, Long id) {
        User user = resolveUser(username);
        Todo todo = resolveTodo(id, user);
        todo.setCompleted(!todo.isCompleted());
        todoRepository.save(todo);
    }

    @Override
    public void deleteTodo(String username, Long id) {
        User user = resolveUser(username);
        Todo todo = resolveTodo(id, user);
        todoRepository.delete(todo);
    }

    private User resolveUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private Todo resolveTodo(Long id, User user) {
        return todoRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + id));
    }
}
