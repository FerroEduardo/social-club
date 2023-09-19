package com.softawii.social.repository.mapper;

import com.softawii.social.model.User;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setAvatarImageId(rs.getLong("avatar_id"));
        user.setSmallAvatarImageId(rs.getLong("mini_avatar_id"));

        return user;
    }
}
