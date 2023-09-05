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

    public Optional<Image> findById(Long id) {
        String sql = """
                SELECT id, blob, s3, local, extension FROM social.image
                WHERE id = :id
                """;

        return jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .query(imageRowMapper)
                .optional();
    }
}
