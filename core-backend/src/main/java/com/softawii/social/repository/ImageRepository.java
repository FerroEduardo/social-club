package com.softawii.social.repository;

import com.softawii.social.model.Image;
import com.softawii.social.repository.mapper.ImageRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class ImageRepository {
    private final JdbcClient     jdbcClient;
    private final ImageRowMapper imageRowMapper;

    public ImageRepository(JdbcClient jdbcClient, ImageRowMapper imageRowMapper) {
        this.jdbcClient = jdbcClient;
        this.imageRowMapper = imageRowMapper;
    }

    public Optional<Image> findByIdActive(Long id) {
        String sql = """
                SELECT id, blob, s3, local, extension FROM social.image
                WHERE id = :id AND deleted_at IS NULL
                """;

        return jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .query(imageRowMapper)
                .optional();
    }

    @Transactional
    public void softDelete(Long id) {
        String sql = """
                UPDATE social.image
                SET deleted_at = CURRENT_TIMESTAMP
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
                UPDATE social.image
                SET deleted_at = CURRENT_TIMESTAMP
                WHERE id = (SELECT id FROM social.post WHERE id = :postId LIMIT 1)
                """;

        jdbcClient
                .sql(sql)
                .param("postId", postId, Types.BIGINT)
                .update();
    }
}
