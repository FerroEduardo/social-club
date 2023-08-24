package com.softawii.social.service;

import com.softawii.social.model.Game;
import com.softawii.social.model.Image;
import com.softawii.social.model.Post;
import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.post.PostDTO;
import com.softawii.social.repository.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Optional;

@Component
public class PostService {
    private static final String         IMAGE_URL_PREFIX = "http://localhost:8099/image/";
    private final        PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Optional<PostDTO> findById(Long id) {
        Optional<Map<String, Object>> optional = repository.findPostsNew(IMAGE_URL_PREFIX, id, PageRequest.of(0, 1)).stream().findFirst();
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> map = optional.get();

        return Optional.of(
                new PostDTO(
                        Long.valueOf(map.get("id").toString()),
                        (String) map.get("description"),
                        Long.valueOf(map.get("reputation").toString()),
                        ZonedDateTime.parse(map.get("createdAt").toString()),
                        ZonedDateTime.parse(map.get("modifiedAt").toString()),
                        Long.valueOf(map.get("authorId").toString()),
                        (String) map.get("authorName"),
                        (String) map.get("authorImageUrl"),
                        Long.valueOf(map.get("gameId").toString()),
                        (String) map.get("gameName"),
                        (String) map.get("gameStudio"),
                        (String) map.get("imageUrl")
                )
        );
    }

    public Page<Map<String, Object>> findAll() {
        return repository.findPostsNew(IMAGE_URL_PREFIX, null, Pageable.unpaged());
    }

    public Page<Map<String, Object>> findAll(int page, int size) {
        return repository.findPostsNew(IMAGE_URL_PREFIX, null, PageRequest.of(page, size, Sort.by(Direction.DESC, "created_at")));
    }

    public Post create(User user, Game game, Image image, String description) {
        Post post = new Post();
        post.setUser(user);
        post.setGame(game);
        post.setDescription(description);
        post.setImage(image);

        return repository.save(post);
    }
}
