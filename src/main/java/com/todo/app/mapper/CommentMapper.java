package com.todo.app.mapper;

import com.todo.app.dto.CommentResponse;
import com.todo.app.entity.Comment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {

    public CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .authorUsername(comment.getUser().getUsername())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}
