package com.softawii.social.repository;

import com.softawii.social.model.PostVote;
import com.softawii.social.repository.mapper.PostVoteRowMapper;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Types;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public class PostVoteRepository {
    private final JdbcClient        jdbcClient;
    private final PostVoteRowMapper postVoteRowMapper;

    public PostVoteRepository(JdbcClient jdbcClient, PostVoteRowMapper postVoteRowMapper) {
        this.jdbcClient = jdbcClient;
        this.postVoteRowMapper = postVoteRowMapper;
    }

    public Optional<PostVote> findById(Long userId, Long postId) {
        String sql = """
                SELECT user_id, post_id, value, created_at, modified_at from social.post_vote
                WHERE user_id = :user_id AND post_id = :post_id
                LIMIT 1
                """;

        return jdbcClient
                .sql(sql)
                .param("user_id", userId, Types.BIGINT)
                .param("post_id", postId, Types.BIGINT)
                .query(postVoteRowMapper)
                .optional();
    }

    public long userReputation(Long userId) {
        String sql = """
                SELECT SUM(pv.value) as "reputation" FROM social.post_vote pv
                WHERE pv.user_id = :userId
                """;

        SqlRowSet rowSet = jdbcClient
                .sql(sql)
                .param("userId", userId, Types.BIGINT)
                .query()
                .rowSet();

        rowSet.next();

        return rowSet
                .getInt("reputation");
    }

    @Transactional
    public PostVote create(PostVote postVote) {
        String sql = """
                INSERT INTO social.post_vote (user_id, post_id, value)
                VALUES (:user_id, :post_id, :value)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("user_id", postVote.getUserId(), Types.BIGINT)
                .param("post_id", postVote.getPostId(), Types.BIGINT)
                .param("value", postVote.getValue(), Types.SMALLINT)
                .update(keyHolder);

        return postVote;
    }

    @Transactional
    public PostVote update(PostVote postVote) {
        String sql = """
                UPDATE social.post_vote
                SET value = :value
                WHERE user_id = :user_id AND post_id = :post_id
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient
                .sql(sql)
                .param("user_id", postVote.getUserId(), Types.BIGINT)
                .param("post_id", postVote.getPostId(), Types.BIGINT)
                .param("value", postVote.getValue(), Types.BIGINT)
                .update(keyHolder);

        return postVote;
    }

    public Long getReputationByPostId(Long postId) {
        String sql = """
                SELECT SUM(pl.value) FROM social.post_vote pl
                WHERE pl.post_id = :post_id
                """;
        return jdbcClient
                .sql(sql)
                .param("post_id", postId, Types.BIGINT)
                .query()
                .singleValue();
    }

}
