package com.todo.app.dto;

import lombok.Data;

@Data
public class TodoRequest {

    private String title;
    private String description;
}
