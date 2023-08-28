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
    private final        PostRepository repository;

    public PostService(PostRepository repository) {
        this.repository = repository;
    }

    public Optional<PostDTO> findById(Long id, Long userId) {
        Optional<Map<String, Object>> optional = repository.findPosts(ImageService.IMAGE_URL_PREFIX, id, userId, PageRequest.of(0, 1)).stream().findFirst();
        if (optional.isEmpty()) {
            return Optional.empty();
        }

        Map<String, Object> map  = optional.get();
        PostDTO             post = new PostDTO();
        post.setId(Long.valueOf(map.get("id").toString()))
                .setDescription((String) map.get("description"))
                .setTitle((String) map.get("title"))
                .setReputation(Long.valueOf(map.get("reputation").toString()))
                .setCreatedAt(ZonedDateTime.parse(map.get("createdAt").toString()))
                .setModifiedAt(ZonedDateTime.parse(map.get("modifiedAt").toString()))
                .setAuthorId(Long.valueOf(map.get("authorId").toString()))
                .setAuthorName((String) map.get("authorName"))
                .setAuthorImageUrl((String) map.get("authorImageUrl"))
                .setGameId(Long.valueOf(map.get("gameId").toString()))
                .setGameName((String) map.get("gameName"))
                .setGameStudio((String) map.get("gameStudio"))
                .setImageUrl((String) map.get("imageUrl"))
                .setUserVote((Short) map.get("userVote"));

        return Optional.of(post);
    }

    public Optional<PostDTO> findById(Long id) {
        return this.findById(id, null);
    }

    public Page<Map<String, Object>> findAll(Long userId) {
        return repository.findPosts(ImageService.IMAGE_URL_PREFIX, null, userId, Pageable.unpaged());
    }

    public Page<Map<String, Object>> findAll() {
        return this.findAll(null);
    }

    public Page<Map<String, Object>> findAll(Long userId, int page, int size) {
        return repository.findPosts(ImageService.IMAGE_URL_PREFIX, null, userId, PageRequest.of(page, size, Sort.by(Direction.DESC, "created_at")));
    }

    public Page<Map<String, Object>> findAll(int page, int size) {
        return this.findAll(null, page, size);
    }

    public Post create(User user, Game game, Image image, String title, String description) {
        Post post = new Post();
        post.setUser(user);
        post.setGame(game);
        post.setTitle(title);
        post.setDescription(description);
        post.setImage(image);

        return repository.save(post);
    }
}
