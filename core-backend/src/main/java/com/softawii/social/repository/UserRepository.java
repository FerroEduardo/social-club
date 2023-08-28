package com.softawii.social.repository;

import com.softawii.social.model.User;
import com.softawii.social.model.dto.request.user.UserDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT new com.softawii.social.model.dto.request.user.UserDTO(u.id, u.name, u.email, CONCAT(com.softawii.social.service.ImageService.IMAGE_URL_PREFIX, u.imageId)) FROM User u where u.email = :email")
    Optional<UserDTO> findByEmailSafe(String email);
    Boolean existsByEmail(String email);
}