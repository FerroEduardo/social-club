package com.softawii.social.service;

import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.user.UserDTO;
import com.softawii.social.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserService {

    private final UserRepository repository;

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public Optional<UserDTO> findByEmailSafe(String email) {
        return repository.findByEmailSafe(email);
    }
}
