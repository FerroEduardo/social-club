package com.softawii.social.repository.mapper;

import com.softawii.social.model.dto.PostDTO;
import com.softawii.social.service.ImageService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

@Component
public class PostDtoRowMapper implements RowMapper<PostDTO> {
    private final ImageService imageService;

    public PostDtoRowMapper(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public PostDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostDTO post = new PostDTO();
        post.setId(rs.getLong("id"));
        post.setDescription(rs.getString("description"));
        post.setTitle(rs.getString("title"));
        post.setReputation(rs.getLong("reputation"));
        post.setCreatedAt(rs.getObject("createdAt", OffsetDateTime.class).toZonedDateTime());
        post.setModifiedAt(rs.getObject("modifiedAt", OffsetDateTime.class).toZonedDateTime());
        post.setAuthorId(rs.getLong("authorId"));
        post.setAuthorName(rs.getString("authorName"));
        post.setAuthorImageUrl(imageService.getImageUrlFromImageId(rs.getLong("authorImageId")));
        post.setGameId(rs.getLong("gameId"));
        post.setGameName(rs.getString("gameName"));
        post.setGameStudio(rs.getString("gameStudio"));
        post.setImageUrl(imageService.getImageUrlFromImageId(rs.getLong("imageId")));
        post.setUserVote(rs.getShort("userVote"));
        post.setGameImageUrl(rs.getString("gameImageUrl"));

        return post;
    }
}
