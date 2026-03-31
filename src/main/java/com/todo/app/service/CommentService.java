package com.todo.app.service;

import com.todo.app.dto.CommentRequest;
import com.todo.app.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    List<CommentResponse> getCommentsForTodo(Long todoId);

    CommentResponse addComment(String username, Long todoId, CommentRequest request);

    void deleteComment(String username, Long commentId);
}
