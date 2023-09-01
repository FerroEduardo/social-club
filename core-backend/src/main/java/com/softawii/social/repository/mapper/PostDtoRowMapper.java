package com.softawii.social.repository.mapper;

import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.service.ImageService;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class PostDtoRowMapper implements RowMapper<PostDTO> {
    public static final PostDtoRowMapper INSTANCE = new PostDtoRowMapper();

    private PostDtoRowMapper() {}

    @Override
    public PostDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        PostDTO post = new PostDTO();
        post.setId(rs.getLong("id"))
                .setDescription(rs.getString("description"))
                .setTitle(rs.getString("title"))
                .setReputation(rs.getLong("reputation"))
                .setCreatedAt(rs.getObject("createdAt", OffsetDateTime.class).toZonedDateTime())
                .setModifiedAt(rs.getObject("modifiedAt", OffsetDateTime.class).toZonedDateTime())
                .setAuthorId(rs.getLong("authorId"))
                .setAuthorName(rs.getString("authorName"))
                .setAuthorImageUrl(ImageService.getImageUrlFromImageId(rs.getLong("authorImageId")))
                .setGameId(rs.getLong("gameId"))
                .setGameName(rs.getString("gameName"))
                .setGameStudio(rs.getString("gameStudio"))
                .setImageUrl(ImageService.getImageUrlFromImageId(rs.getLong("imageId")))
                .setUserVote(rs.getShort("userVote"));

        return post;
    }
}
