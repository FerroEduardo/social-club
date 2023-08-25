package com.softawii.social.repository;

import com.softawii.social.model.Post;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Map;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query(value = """
            SELECT
                p.id,
                p.description,
                COALESCE((SELECT pr.reputation FROM social.post_reputation pr WHERE pr.post_id = p.id), 0) AS reputation,
                p.created_at AS "createdAt",
                p.modified_at AS "modifiedAt",
                u.id AS "authorId",
                u.name AS "authorName",
                u.image_url AS "authorImageUrl",
                p.game_id AS "gameId",
                g.name AS "gameName",
                g.studio AS "gameStudio",
                CONCAT(:image_url, p.image_id) AS "imageUrl",
                pv.value AS "userVote"
            FROM social.post p
                     INNER JOIN social.user u ON u.id = p.author_id
                     INNER JOIN social.game g ON g.id = p.game_id
                     LEFT JOIN social.post_vote pv ON pv.post_id = p.id AND pv.user_id = :user_id
            WHERE (:post_id IS NULL OR :post_id = p.id)
            """,
           countQuery = "SELECT COUNT(p) FROM social.post p WHERE (:post_id IS NULL OR :post_id = p.id) AND (:image_url = :image_url) AND (:user_id = :user_id)",
           nativeQuery = true)
    Page<Map<String, Object>> findPosts(
            @Param("image_url") String image_url,
            @Param("post_id") Long post_id,
            @Param("user_id") Long user_id,
            Pageable pageable
    );
}
