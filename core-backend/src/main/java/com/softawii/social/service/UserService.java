package com.softawii.social.service;

import com.softawii.social.model.User;
import com.softawii.social.model.dto.UserDTO;
import com.softawii.social.repository.PostVoteRepository;
import com.softawii.social.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private final UserRepository     userRepository;
    private final PostVoteRepository postVoteRepository;

    public UserService(UserRepository userRepository, PostVoteRepository postVoteRepository) {
        this.userRepository = userRepository;
        this.postVoteRepository = postVoteRepository;
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<UserDTO> findByEmailSafe(String email) {
        return userRepository.findByEmailSafe(email);
    }

    public long userReputation(Long userId) {
        return postVoteRepository.userReputation(userId);
    }
}
