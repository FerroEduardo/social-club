package com.softawii.social.repository.mapper;

import com.softawii.social.model.Game;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GameRowMapper implements RowMapper<Game> {
    @Override
    public Game mapRow(ResultSet rs, int rowNum) throws SQLException {
        Game game = new Game();
        game.setId(rs.getLong("id"));
        game.setName(rs.getString("name"));
        game.setStudio(rs.getString("studio"));

        return game;
    }
}
