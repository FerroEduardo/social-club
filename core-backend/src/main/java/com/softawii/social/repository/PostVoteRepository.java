package com.softawii.social.repository;

import com.softawii.social.model.PostVote;
import com.softawii.social.model.embeddable.PostVoteId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PostVoteRepository extends JpaRepository<PostVote, PostVoteId> {
    @Query(value = """
            SELECT SUM(pl.value) FROM PostVote pl
            WHERE pl.postId = :postId
            """)
    Long getReputationByPostId(Long postId);

    Optional<PostVote> findByUserId(Long userId);
}
