package com.softawii.social.service;

import com.softawii.social.model.Comment;
import com.softawii.social.model.dto.request.comment.CommentDTO;
import com.softawii.social.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CommentService {
    public static final Pattern COMMENT_VALUE_PATTERN = Pattern.compile("(\n{2,2})(\n*)", Pattern.CASE_INSENSITIVE);
    private final CommentRepository repository;

    public CommentService(CommentRepository repository) {
        this.repository = repository;
    }

    public CommentDTO create(Long postId, Long authorId, String value) {
        Comment comment = repository.create(new Comment(authorId, postId, parseCommentValue(value)));

        return repository.findById(comment.getId()).get();
    }

    public Page<CommentDTO> findAll(Long postId, int page, int size) {
        return repository.findByPostId(postId, PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created_at")), true);
    }

    public void delete(Long commentId, Long authorId) {
        CommentDTO comment = repository.findByIdAndUserId(commentId, authorId).orElseThrow();
        repository.softDelete(commentId);
    }

    private String parseCommentValue(String value) {
        Matcher matcher = CommentService.COMMENT_VALUE_PATTERN.matcher(value);
        return matcher.replaceAll("$1").trim();
    }
}
