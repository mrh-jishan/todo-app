package com.todo.app.mapper;

import com.todo.app.dto.TodoResponse;
import com.todo.app.entity.Todo;
import org.springframework.stereotype.Component;

@Component
public class TodoMapper {

    public TodoResponse toResponse(Todo todo) {
        return TodoResponse.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .description(todo.getDescription())
                .completed(todo.isCompleted())
                .ownerUsername(todo.getUser().getUsername())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }
}
