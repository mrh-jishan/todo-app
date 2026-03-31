package com.todo.app.service.impl;

import com.todo.app.dto.CommentRequest;
import com.todo.app.dto.CommentResponse;
import com.todo.app.entity.Comment;
import com.todo.app.entity.Todo;
import com.todo.app.entity.User;
import com.todo.app.mapper.CommentMapper;
import com.todo.app.repository.CommentRepository;
import com.todo.app.repository.TodoRepository;
import com.todo.app.repository.UserRepository;
import com.todo.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;
    private final CommentMapper commentMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentsForTodo(Long todoId) {
        Todo todo = resolveTodo(todoId);
        return commentRepository.findByTodoOrderByCreatedAtAsc(todo).stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CommentResponse addComment(String username, Long todoId, CommentRequest request) {
        User user = resolveUser(username);
        Todo todo = resolveTodo(todoId);
        Comment comment = Comment.builder()
                .content(request.getContent())
                .todo(todo)
                .user(user)
                .build();
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(String username, Long commentId) {
        User user = resolveUser(username);
        Comment comment = commentRepository.findByIdAndUser(commentId, user)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found or not owned by you"));
        commentRepository.delete(comment);
    }

    private User resolveUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    private Todo resolveTodo(Long todoId) {
        return todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("Todo not found: " + todoId));
    }
}
