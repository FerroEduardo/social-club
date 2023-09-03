package com.softawii.social.repository;

import com.softawii.social.model.Game;
import com.softawii.social.repository.mapper.GameRowMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class GameRepository {
    private final JdbcClient    jdbcClient;
    private final GameRowMapper gameRowMapper;

    public GameRepository(JdbcClient jdbcClient, GameRowMapper gameRowMapper) {
        this.jdbcClient = jdbcClient;
        this.gameRowMapper = gameRowMapper;
    }

    public Optional<Game> findById(Long id) {
        String sql = """
                SELECT id, name, studio FROM social.game
                WHERE id = :id
                """;

        return jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .query(gameRowMapper)
                .optional();
    }

    public Page<Game> findAll(String name, Pageable pageable) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        String sql = """
                SELECT id, name, studio FROM social.game
                WHERE (:name IS NULL OR LOWER(name) LIKE LOWER(:name))
                """;
        if (name != null) {
            parameterSource.addValue("name", '%' + name + '%', Types.VARCHAR);
        } else {
            parameterSource.addValue("name", null, Types.VARCHAR);
        }
        if (pageable.isPaged()) {
            sql += "\nLIMIT :size OFFSET :offset";
            parameterSource.addValue("offset", pageable.getOffset(), Types.INTEGER);
            parameterSource.addValue("size", pageable.getPageSize(), Types.INTEGER);
        }

        List<Game> content = jdbcClient
                .sql(sql)
                .paramSource(parameterSource)
                .query(gameRowMapper)
                .list();


        return PageableExecutionUtils.getPage(content, pageable, () -> {
            String countQuery = "SELECT COUNT(*) FROM social.game WHERE (:name IS NULL OR LOWER(name) LIKE LOWER(:name))";

            MapSqlParameterSource parameterSourceCount = new MapSqlParameterSource();
            if (name != null) {
                parameterSourceCount.addValue("name", '%' + name + '%', Types.VARCHAR);
            } else {
                parameterSourceCount.addValue("name", null, Types.VARCHAR);
            }

            return jdbcClient
                    .sql(countQuery)
                    .paramSource(parameterSourceCount)
                    .query()
                    .singleValue();
        });
    }

    @Transactional
    public Game create(Game game) {
        String sql = """
                INSERT INTO social.game (name, studio)
                VALUES (:name, :studio)
                RETURNING id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("name", game.getName(), Types.VARCHAR)
                .param("studio", game.getStudio(), Types.VARCHAR)
                .update(keyHolder);

        game.setId(keyHolder.getKey().longValue());

        return game;
    }

    @Transactional
    public Game update(Game game) {
        String sql = """
                UPDATE social.game
                SET name = :name, studio = :studio
                WHERE id = :id
                """;

        jdbcClient
                .sql(sql)
                .param("id", game.getId(), Types.BIGINT)
                .param("name", game.getName(), Types.VARCHAR)
                .param("studio", game.getStudio(), Types.VARCHAR)
                .update();

        return game;
    }

    @Transactional
    public void delete(Long id) {
        String sql = """
                DELETE FROM social.game
                WHERE id = :id
                """;

        jdbcClient
                .sql(sql)
                .param("id", id, Types.BIGINT)
                .update();
    }
}
