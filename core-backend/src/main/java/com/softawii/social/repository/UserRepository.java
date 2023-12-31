package com.softawii.social.repository;

import com.softawii.social.model.User;
import com.softawii.social.model.dto.UserDTO;
import com.softawii.social.repository.mapper.UserDtoRowMapper;
import com.softawii.social.repository.mapper.UserRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class UserRepository {
    private final JdbcClient       jdbcClient;
    private final UserRowMapper    userRowMapper;
    private final UserDtoRowMapper userDtoRowMapper;

    public UserRepository(JdbcClient jdbcClient, UserRowMapper userRowMapper, UserDtoRowMapper userDtoRowMapper) {
        this.jdbcClient = jdbcClient;
        this.userRowMapper = userRowMapper;
        this.userDtoRowMapper = userDtoRowMapper;
    }

    @Transactional
    public User create(User user) {
        String sql = """
                INSERT INTO social.user (name, email, avatar_id, mini_avatar_id)
                VALUES (:name, :email, :avatar_id, :mini_avatar_id)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("name", user.getName(), Types.VARCHAR)
                .param("email", user.getEmail(), Types.VARCHAR)
                .param("avatar_id", user.getAvatarImageId(), Types.BIGINT)
                .param("mini_avatar_id", user.getSmallAvatarImageId(), Types.BIGINT)
                .update(keyHolder);

        user.setId(keyHolder.getKey().longValue());

        return user;
    }

    public Optional<User> findByEmail(String email) {
        String sql = """
                SELECT id, name, email, avatar_id, mini_avatar_id from social.user
                WHERE email = :email
                LIMIT 1
                """;

        return jdbcClient
                .sql(sql)
                .param("email", email, Types.VARCHAR)
                .query(userRowMapper)
                .optional();
    }

    public Optional<UserDTO> findByEmailSafe(String email) {
        String sql = """
                SELECT id, name, email, avatar_id, mini_avatar_id from social.user
                WHERE email = :email
                LIMIT 1
                """;

        return jdbcClient
                .sql(sql)
                .param("email", email, Types.VARCHAR)
                .query(userDtoRowMapper)
                .optional();
    }
}