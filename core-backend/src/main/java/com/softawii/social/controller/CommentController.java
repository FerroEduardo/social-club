package com.softawii.social.controller;

import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(value = "comment")
@Validated
public class CommentController {
    private final CommentService service;

    public CommentController(CommentService service) {
        this.service = service;
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<?> delete(@PathVariable Long commentId, OAuth2AuthenticationToken authentication) {
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        try {
            service.delete(commentId, principal.getId());

            return ResponseEntity.ok().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "Comment not found"));
        }
    }
}
