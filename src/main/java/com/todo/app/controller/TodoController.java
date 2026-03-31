package com.todo.app.controller;

import com.todo.app.dto.TodoRequest;
import com.todo.app.service.TodoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;

    @GetMapping
    public String listTodos(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        model.addAttribute("todos", todoService.getTodosForUser(userDetails.getUsername()));
        model.addAttribute("username", userDetails.getUsername());
        return "todos/list";
    }

    @GetMapping("/new")
    public String newTodoForm(Model model) {
        model.addAttribute("todoRequest", new TodoRequest());
        model.addAttribute("todoId", null);
        return "todos/form";
    }

    @PostMapping
    public String createTodo(@AuthenticationPrincipal UserDetails userDetails,
                             @ModelAttribute TodoRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            todoService.createTodo(userDetails.getUsername(), request);
            redirectAttributes.addFlashAttribute("successMessage", "Todo created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/todos";
    }

    @GetMapping("/{id}/edit")
    public String editTodoForm(@AuthenticationPrincipal UserDetails userDetails,
                               @PathVariable Long id,
                               Model model) {
        model.addAttribute("todoRequest", todoService.getTodoById(userDetails.getUsername(), id));
        model.addAttribute("todoId", id);
        return "todos/form";
    }

    @PostMapping("/{id}")
    public String updateTodo(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id,
                             @ModelAttribute TodoRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            todoService.updateTodo(userDetails.getUsername(), id, request);
            redirectAttributes.addFlashAttribute("successMessage", "Todo updated.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/todos";
    }

    @PostMapping("/{id}/toggle")
    public String toggleTodo(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            todoService.toggleTodo(userDetails.getUsername(), id);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/todos";
    }

    @PostMapping("/{id}/delete")
    public String deleteTodo(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long id,
                             RedirectAttributes redirectAttributes) {
        try {
            todoService.deleteTodo(userDetails.getUsername(), id);
            redirectAttributes.addFlashAttribute("successMessage", "Todo deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/todos";
    }
}
