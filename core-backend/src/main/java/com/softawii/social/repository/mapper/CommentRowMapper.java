package com.softawii.social.repository.mapper;

import com.softawii.social.model.Comment;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class CommentRowMapper implements RowMapper<Comment> {
    @Override
    public Comment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Comment comment = new Comment();
        comment.setId(rs.getLong("id"));
        comment.setAuthorId(rs.getLong("author_id"));
        comment.setPostId(rs.getLong("post_id"));
        comment.setValue(rs.getString("value"));
        comment.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class).toZonedDateTime());
        comment.setModifiedAt(rs.getObject("modified_at", OffsetDateTime.class).toZonedDateTime());
        comment.setDeletedAt(rs.getObject("deleted_at", OffsetDateTime.class).toZonedDateTime());

        return comment;
    }
}
