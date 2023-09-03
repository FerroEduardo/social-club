package com.softawii.social.repository.mapper;

import com.softawii.social.model.PostVote;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class PostVoteRowMapper implements RowMapper<PostVote> {
    @Override
    public PostVote mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostVote postVote = new PostVote();
        postVote.setValue(rs.getLong("value"));
        postVote.setPostId(rs.getLong("post_id"));
        postVote.setUserId(rs.getLong("user_id"));
        postVote.setModifiedAt(rs.getObject("modified_at", OffsetDateTime.class).toZonedDateTime());
        postVote.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class).toZonedDateTime());

        return postVote;
    }
}
