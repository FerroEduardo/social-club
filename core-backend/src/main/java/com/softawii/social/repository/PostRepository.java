package com.softawii.social.repository;

import com.softawii.social.model.Post;
import com.softawii.social.model.dto.request.post.PostDTO;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT NEW com.softawii.social.model.dto.request.post.PostDTO(
                p.id,
                p.description,
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

    @Query(value = """
            SELECT
                p.id,
                p.description,
                COALESCE((SELECT SUM(pv.value) FROM social.post_vote pv WHERE PV.post_id = p.id LIMIT 1), 0) AS reputation,
                p.created_at AS "createdAt",
                p.modified_at AS "modifiedAt",
                u.id AS "authorId",
                u.name AS "authorName",
                u.image_url AS "authorImageUrl",
                p.game_id AS "gameId",
                g.name AS "gameName",
                g.studio AS "gameStudio",
                CONCAT(:image_url, p.image_id) AS "imageUrl"
            FROM social.post p
                     INNER JOIN social.user u ON u.id = p.author_id
                     INNER JOIN social.game g ON g.id = p.game_id
            WHERE (:post_id IS NULL OR :post_id = p.id)
            """,
           countQuery = "SELECT COUNT(p) FROM social.post p WHERE (:post_id IS NULL OR :post_id = p.id) AND (:image_url = :image_url)",
           nativeQuery = true)
    Page<Map<String, Object>> findPostsNew(@Param("image_url") String image_url, @Param(
            "post_id") Long post_id, Pageable pageable);
}
