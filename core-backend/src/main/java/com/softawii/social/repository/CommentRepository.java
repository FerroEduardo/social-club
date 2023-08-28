package com.softawii.social.repository;

import com.softawii.social.model.Comment;
import com.softawii.social.model.dto.request.comment.CommentDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);

    @Query(
            value = """
                    SELECT new com.softawii.social.model.dto.request.comment.CommentDTO(c.id, c.authorId, u.name, c.value, c.createdAt, concat(com.softawii.social.service.ImageService.IMAGE_URL_PREFIX, u.imageId) )
                    FROM Comment c INNER join User u ON u.id = c.authorId WHERE c.postId = :postId AND c.deletedAt IS NULL
                    """,
            countQuery = "SELECT count(*) FROM Comment c WHERE c.postId = :postId AND c.deletedAt IS NULL")
    Page<CommentDTO> findAllByPostIdSafe(Long postId, Pageable pageable);

    @Query(
            value = """
                    SELECT new com.softawii.social.model.dto.request.comment.CommentDTO(c.id, c.authorId, u.name, c.value, c.createdAt, concat(com.softawii.social.service.ImageService.IMAGE_URL_PREFIX, u.imageId))
                    FROM Comment c INNER join User u ON u.id = c.authorId WHERE c.id = :id AND c.deletedAt IS NULL
                    """)
    Optional<CommentDTO> findByIdSafe(Long id);

    Optional<Comment> findByIdAndAuthorId(Long id, Long authorId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE social.post_comment c SET deleted_at = CURRENT_TIMESTAMP WHERE c.id = :commentId",
           nativeQuery = true)
    void softDelete(Long commentId);
}