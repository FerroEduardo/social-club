package com.softawii.social.repository;

import com.softawii.social.model.Comment;
import com.softawii.social.model.dto.CommentDTO;
import com.softawii.social.repository.mapper.CommentDtoRowMapper;
import com.softawii.social.repository.mapper.CommentRowMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class CommentRepository {
    private final JdbcClient          jdbcClient;
    private final CommentRowMapper    commentRowMapper;
    private final CommentDtoRowMapper commentDtoRowMapper;

    public CommentRepository(JdbcClient jdbcClient, CommentRowMapper commentRowMapper, CommentDtoRowMapper commentDtoRowMapper) {
        this.jdbcClient = jdbcClient;
        this.commentRowMapper = commentRowMapper;
        this.commentDtoRowMapper = commentDtoRowMapper;
    }

//    public Optional<Comment> findById(Long id) {
//        String sql = """
//                SELECT id, author_id, post_id, value, created_at, modified_at, deleted_at FROM social.post_comment
//                WHERE id = :id
//                """;
//
//        return jdbcClient
//                .sql(sql)
//                .param("id", id, Types.BIGINT)
//                .query(commentRowMapper)
//                .optional();
//    }

    public Optional<CommentDTO> findById(Long id) {
        String sql = """
                SELECT pc.id, pc.author_id, u.name as "author_name", u.avatar_id AS "authorAvatarId", u.mini_avatar_id AS "miniAuthorAvatarId", pc.value, pc.created_at
                FROM social.post_comment pc
                INNER JOIN social.user u on u.id = author_id
                WHERE pc.id = :id
                """;

        return jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .query(commentDtoRowMapper)
                .optional();
    }

    public Page<CommentDTO> findByPostId(Long postId, Pageable pageable, boolean isActive) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = """
                SELECT pc.id, pc.author_id, u.name AS "author_name", u.avatar_id AS "authorAvatarId", u.mini_avatar_id AS "miniAuthorAvatarId", pc.value, pc.created_at
                FROM social.post_comment pc
                INNER JOIN social.user u ON u.id = author_id
                WHERE pc.post_id = :post_id
                  AND CASE
                          WHEN :active = TRUE THEN pc.deleted_at IS NULL
                          ELSE TRUE
                    END
                """;

        parameterSource.addValue("post_id", postId, Types.BIGINT);
        parameterSource.addValue("active", isActive, Types.BOOLEAN);

        String orders = pageable.getSort().stream().map(order -> String.format("%s %s", order.getProperty(), order.getDirection().name())).collect(Collectors.joining(", "));
        if (!orders.isBlank()) {
            sql += "\nORDER BY " + orders;
        }
        if (pageable.isPaged()) {
            sql += "\nLIMIT :size OFFSET :offset";
            parameterSource.addValue("offset", pageable.getOffset(), Types.INTEGER);
            parameterSource.addValue("size", pageable.getPageSize(), Types.INTEGER);
        }

        List<CommentDTO> content = jdbcClient
                .sql(sql)
                .paramSource(parameterSource)
                .query(commentDtoRowMapper)
                .list();

        return PageableExecutionUtils.getPage(content, pageable, () -> {
            String countQuery = """
                    SELECT COUNT(*) FROM social.post_comment pc
                    WHERE pc.post_id = :post_id
                      AND CASE
                              WHEN :active = TRUE THEN pc.deleted_at IS NULL
                              ELSE TRUE
                        END
                    """;

            return jdbcClient
                    .sql(countQuery)
                    .param("post_id", postId, Types.BIGINT)
                    .param("active", isActive, Types.BOOLEAN)
                    .query()
                    .singleValue();
        });
    }

    public Optional<CommentDTO> findByIdAndUserId(Long id, Long userId) {
        String sql = """
                SELECT pc.id, pc.author_id, u.name as "author_name", u.avatar_id AS "authorAvatarId", u.mini_avatar_id AS "miniAuthorAvatarId", pc.value, pc.created_at
                FROM social.post_comment pc
                INNER JOIN social.user u on u.id = author_id
                WHERE pc.id = :id AND pc.author_id = :author_id AND pc.deleted_at IS NULL
                """;

        return jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .param("author_id", userId, Types.BIGINT)
                .query(commentDtoRowMapper)
                .optional();
    }

    @Transactional
    public void softDelete(Long id) {
        String sql = """
                UPDATE social.post_comment
                SET deleted_at = CURRENT_TIMESTAMP, modified_at = CURRENT_TIMESTAMP
                WHERE id = :id
                """;

        jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .update();
    }

    @Transactional
    public void softDeleteByPostId(Long postId) {
        String sql = """
                UPDATE social.post_comment
                SET deleted_at = CURRENT_TIMESTAMP, modified_at = CURRENT_TIMESTAMP
                WHERE post_id = :post_id
                """;

        jdbcClient
                .sql(sql)
                .param("post_id", postId, Types.BIGINT)
                .update();
    }

    @Transactional
    public Comment create(Comment comment) {
        String sql = """
                INSERT INTO social.post_comment (author_id, post_id, value)
                VALUES (:author_id, :post_id, :value)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("author_id", comment.getAuthorId(), Types.BIGINT)
                .param("post_id", comment.getPostId(), Types.BIGINT)
                .param("value", comment.getValue(), Types.VARCHAR)
                .update(keyHolder);

        comment.setId(keyHolder.getKey().longValue());

        return comment;
    }
}
