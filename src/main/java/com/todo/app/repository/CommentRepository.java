package com.todo.app.repository;

import com.todo.app.entity.Comment;
import com.todo.app.entity.Todo;
import com.todo.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTodoOrderByCreatedAtAsc(Todo todo);

    Optional<Comment> findByIdAndUser(Long id, User user);
}
