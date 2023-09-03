package com.softawii.social.repository;

import com.softawii.social.model.Post;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.repository.mapper.PostDtoRowMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class PostRepository {
    private final JdbcClient       jdbcClient;
    private final PostDtoRowMapper postDtoRowMapper;

    public PostRepository(JdbcClient jdbcClient, PostDtoRowMapper postDtoRowMapper) {
        this.jdbcClient = jdbcClient;
        this.postDtoRowMapper = postDtoRowMapper;
    }

    @Transactional
    public Post save(Post post) {
        String sql = """
                INSERT INTO social.post (author_id, game_id, title, description, image_id)
                VALUES (:author_id, :game_id, :title, :description, :image_id)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("author_id", post.getUser().getId(), Types.BIGINT)
                .param("game_id", post.getGame().getId(), Types.BIGINT)
                .param("title", post.getTitle(), Types.VARCHAR)
                .param("description", post.getDescription(), Types.VARCHAR)
                .param("image_id", post.getImage().getId(), Types.BIGINT)
                .update(keyHolder);
        post.setId(keyHolder.getKey().longValue());

        return post;
    }

    public Optional<PostDTO> findByIdSafe(Long postId, Long userId) {
        String sql = """
                SELECT
                    p.id,
                    p.title,
                    p.description,
                    COALESCE((SELECT pr.reputation FROM social.post_reputation pr WHERE pr.post_id = p.id), 0) AS reputation,
                    p.created_at AS "createdAt",
                    p.modified_at AS "modifiedAt",
                    u.id AS "authorId",
                    u.name AS "authorName",
                    u.image_id AS "authorImageId",
                    p.game_id AS "gameId",
                    g.name AS "gameName",
                    g.studio AS "gameStudio",
                    p.image_id as "imageId",
                    pv.value AS "userVote"
                FROM social.post p
                         INNER JOIN social.user u ON u.id = p.author_id
                         INNER JOIN social.game g ON g.id = p.game_id
                         LEFT JOIN social.post_vote pv ON pv.post_id = p.id AND pv.user_id = :user_id
                WHERE :post_id = p.id
                LIMIT 1
                """;

        return jdbcClient
                .sql(sql)
                .param("post_id", postId, Types.BIGINT)
                .param("user_id", userId, Types.BIGINT)
                .query(postDtoRowMapper)
                .optional();
    }

    public Page<PostDTO> findAllSafe(int page, int size, Long user_id) {
        String sql = """
                SELECT
                    p.id,
                    p.title,
                    p.description,
                    COALESCE((SELECT pr.reputation FROM social.post_reputation pr WHERE pr.post_id = p.id), 0) AS reputation,
                    p.created_at AS "createdAt",
                    p.modified_at AS "modifiedAt",
                    u.id AS "authorId",
                    u.name AS "authorName",
                    u.image_id AS "authorImageId",
                    p.game_id AS "gameId",
                    g.name AS "gameName",
                    g.studio AS "gameStudio",
                    p.image_id as "imageId",
                    pv.value AS "userVote"
                FROM social.post p
                         INNER JOIN social.user u ON u.id = p.author_id
                         INNER JOIN social.game g ON g.id = p.game_id
                         LEFT JOIN social.post_vote pv ON pv.post_id = p.id AND pv.user_id = :user_id
                WHERE (:user_id IS NULL OR :user_id = u.id)
                ORDER BY p.created_at DESC
                LIMIT :size
                OFFSET :offset
                """;

        List<PostDTO> content = jdbcClient
                .sql(sql)
                .param("user_id", user_id, Types.BIGINT)
                .param("size", size, Types.INTEGER)
                .param("offset", page * size, Types.INTEGER)
                .query(postDtoRowMapper)
                .list();


        return PageableExecutionUtils.getPage(content, PageRequest.of(page, size), () -> {
            String countQuery = """
                    SELECT COUNT(p) FROM social.post p INNER JOIN social.user u ON u.id = p.author_id WHERE (:user_id IS NULL OR :user_id = u.id)
                    """;

            return jdbcClient
                    .sql(countQuery)
                    .param("user_id", user_id, Types.BIGINT)
                    .query()
                    .singleValue();
        });
    }

    public Page<PostDTO> findAllSafe(int page, int size) {
        return this.findAllSafe(page, size, null);
    }

}
