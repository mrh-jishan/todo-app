package com.todo.app.repository;

import com.todo.app.entity.Todo;
import com.todo.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findByUserOrderByCreatedAtDesc(User user);

    Optional<Todo> findByIdAndUser(Long id, User user);

    long countByUserAndCompleted(User user, boolean completed);
}
