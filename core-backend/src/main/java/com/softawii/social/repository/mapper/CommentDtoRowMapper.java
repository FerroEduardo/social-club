package com.softawii.social.repository.mapper;

import com.softawii.social.model.dto.request.comment.CommentDTO;
import com.softawii.social.service.ImageService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class CommentDtoRowMapper implements RowMapper<CommentDTO> {
    private final ImageService imageService;

    public CommentDtoRowMapper(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public CommentDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(rs.getLong("id"));
        commentDTO.setAuthorId(rs.getLong("author_id"));
        commentDTO.setAuthorName(rs.getString("author_name"));
        commentDTO.setAuthorImageUrl(imageService.getImageUrlFromImageId(rs.getLong("author_image_id")));
        commentDTO.setValue(rs.getString("value"));
        commentDTO.setCreatedAt(rs.getObject("created_at", OffsetDateTime.class).toZonedDateTime());

        return commentDTO;
    }
}
