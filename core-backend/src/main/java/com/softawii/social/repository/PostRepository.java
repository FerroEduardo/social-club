package com.softawii.social.repository;

import com.softawii.social.model.Post;
import com.softawii.social.model.dto.request.post.PostDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT NEW com.softawii.social.model.dto.PostDTO(
                p.id,
                p.description,
                p.reputation,
                p.createdAt,
                p.modifiedAt,
                u.id,
                u.name,
                u.imageUrl,
                p.game.id,
                g.name,
                g.studio,
                concat(:image_url, p.image.id))
            FROM Post p
                  INNER JOIN User u on u.id = p.user.id
                  INNER JOIN Game g on g.id = p.game.id
            WHERE (:post_id IS NULL OR :post_id = p.id)
            """,
           countQuery = "SELECT count(p) FROM Post p WHERE (:post_id IS NULL OR :post_id = p.id) AND (:image_url = :image_url)")
    @Transactional(readOnly = true)
    Page<PostDTO> findPosts(@Param("image_url") String image_url, @Param("post_id") Long post_id, Pageable pageable);
}
