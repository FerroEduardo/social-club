package com.softawii.social.repository;

import com.softawii.social.model.Comment;
import com.softawii.social.model.dto.request.comment.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);

    @Query(
            value = "SELECT new com.softawii.social.model.dto.request.comment.CommentDTO(c.id, c.authorId, u.name, c.value, c.createdAt, u.imageUrl)" +
                    "FROM Comment c INNER join User u ON u.id = c.authorId WHERE c.postId = :postId",
            countQuery = "SELECT count(*) FROM Comment c WHERE c.postId = :postId")
    Page<CommentDTO> findAllByPostIdSafe(Long postId, Pageable pageable);

    @Query(
            value = "SELECT new com.softawii.social.model.dto.request.comment.CommentDTO(c.id, c.authorId, u.name, c.value, c.createdAt, u.imageUrl)" +
                    "FROM Comment c INNER join User u ON u.id = c.authorId WHERE c.id = :id")
    Optional<CommentDTO> findByIdSafe(Long id);
}