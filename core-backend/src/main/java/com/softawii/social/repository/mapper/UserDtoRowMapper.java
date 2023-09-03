package com.softawii.social.repository.mapper;

import com.softawii.social.model.dto.request.user.UserDTO;
import com.softawii.social.service.ImageService;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserDtoRowMapper implements RowMapper<UserDTO> {
    private final ImageService imageService;

    public UserDtoRowMapper(ImageService imageService) {
        this.imageService = imageService;
    }

    @Override
    public UserDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(rs.getLong("id"));
        userDTO.setName(rs.getString("name"));
        userDTO.setEmail(rs.getString("email"));
        userDTO.setImageUrl(imageService.getImageUrlFromImageId(rs.getLong("image_id")));

        return userDTO;
    }
}
