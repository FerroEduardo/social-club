package com.softawii.social.repository.mapper;

import com.softawii.social.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRowMapper implements RowMapper<User> {
    public static final UserRowMapper INSTANCE = new UserRowMapper();

    private UserRowMapper() {}

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setImageId(rs.getLong("image_id"));

        return user;
    }
}
