package com.softawii.social.repository;

import com.softawii.social.model.Comment;
import com.softawii.social.model.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}