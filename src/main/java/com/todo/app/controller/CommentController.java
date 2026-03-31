package com.todo.app.controller;

import com.todo.app.dto.CommentRequest;
import com.todo.app.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todos/{todoId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public String addComment(@AuthenticationPrincipal UserDetails userDetails,
                             @PathVariable Long todoId,
                             @ModelAttribute CommentRequest request,
                             RedirectAttributes redirectAttributes) {
        try {
            commentService.addComment(userDetails.getUsername(), todoId, request);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/todos/" + todoId;
    }

    @PostMapping("/{commentId}/delete")
    public String deleteComment(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable Long todoId,
                                @PathVariable Long commentId,
                                RedirectAttributes redirectAttributes) {
        try {
            commentService.deleteComment(userDetails.getUsername(), commentId);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/todos/" + todoId;
    }
}
