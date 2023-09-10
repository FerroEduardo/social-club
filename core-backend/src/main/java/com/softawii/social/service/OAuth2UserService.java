package com.softawii.social.service;

import com.softawii.social.exception.FailedToCreateImageException;
import com.softawii.social.exception.GithubOAuth2MissingEmailException;
import com.softawii.social.exception.OAuth2AuthenticationProcessingException;
import com.softawii.social.model.User;
import com.softawii.social.repository.UserRepository;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.security.oauth.user.OAuth2UserInfo;
import com.softawii.social.security.oauth.user.OAuth2UserInfoFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final ImageService   imageService;

    public OAuth2UserService(UserRepository userRepository, ImageService imageService) {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        try {
            return processOAuth2User(oAuth2UserRequest, oAuth2User);
        } catch (AuthenticationException ex) {
            throw ex;
        } catch (Exception ex) {
            // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) throws FailedToCreateImageException, IOException {
        String         platform       = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(platform, oAuth2User.getAttributes());
        if (!StringUtils.hasLength(oAuth2UserInfo.getEmail())) {
            if (platform.equals("github")) {
                throw new GithubOAuth2MissingEmailException("Public email is not enabled");
            }
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());
        User           user;
        if (userOptional.isEmpty()) {
            Long imageId = uploadUserImage(oAuth2UserInfo);
            user = registerNewUser(oAuth2UserInfo, imageId);
        } else {
            user = userOptional.get();
        }

        return UserPrincipal.create(user, oAuth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserInfo oAuth2UserInfo, Long imageId) {
        User user = new User();
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageId(imageId);

        return userRepository.create(user);
    }

    private Long uploadUserImage(OAuth2UserInfo oAuth2UserInfo) throws FailedToCreateImageException, IOException {
        try (BufferedInputStream in = new BufferedInputStream(new URL(oAuth2UserInfo.getImageUrl()).openStream());
             ByteArrayOutputStream fileOutputStream = new ByteArrayOutputStream()) {
            byte[] dataBuffer = new byte[1024];
            int    bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
            byte[] image = fileOutputStream.toByteArray();
            return imageService.create(image);
        } catch (IOException | FailedToCreateImageException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
