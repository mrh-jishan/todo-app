package com.todo.app.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TodoResponse {

    private Long id;
    private String title;
    private String description;
    private boolean completed;
    private String ownerUsername;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
