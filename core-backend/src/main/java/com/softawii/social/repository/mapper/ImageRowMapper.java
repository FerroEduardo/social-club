package com.softawii.social.repository.mapper;

import com.softawii.social.model.Image;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ImageRowMapper implements RowMapper<Image> {
    @Override
    public Image mapRow(ResultSet rs, int rowNum) throws SQLException {
        Image image = new Image();
        image.setId(rs.getLong("id"));
        image.setBlob(rs.getBytes("blob"));
        image.setS3(rs.getString("s3"));
        image.setLocal(rs.getString("local"));

        return image;
    }
}
