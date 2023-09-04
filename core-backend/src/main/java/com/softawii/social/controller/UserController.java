package com.softawii.social.controller;

import com.softawii.social.model.dto.request.post.IndexPostRequestDTO;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.PostService;
import com.softawii.social.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {

    private final UserService service;
    private final PostService postService;

    public UserController(UserService service, PostService postService) {
        this.service = service;
        this.postService = postService;
    }

    @GetMapping("me")
    public ResponseEntity<?> user(OAuth2AuthenticationToken authentication) throws Exception {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        return ResponseEntity.ok(service.findByEmailSafe(user.getEmail()).get());
    }

    @GetMapping("post")
    public ResponseEntity<?> userPosts(@Valid IndexPostRequestDTO dto, OAuth2AuthenticationToken authentication) {
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();
        Page<PostDTO> post = this.postService.findUserPosts(user.getId(), dto.getPage().intValue(), dto.getSize().intValue());

        return ResponseEntity.ok(post);
    }
}
