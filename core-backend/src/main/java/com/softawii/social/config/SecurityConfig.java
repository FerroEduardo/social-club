package com.softawii.social.config;

import com.softawii.social.exception.GithubOAuth2MissingEmailException;
import com.softawii.social.security.UserPrincipal;
import com.softawii.social.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    private OAuth2UserService oAuth2UserService;

    public SecurityConfig(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(configurer -> {
                    configurer.disable();
                })
                .httpBasic(configurer -> configurer
                        .disable()
                )
                .formLogin(configurer -> configurer
                        .loginPage("/")
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/favicon.ico", "/logout/success", "/login/failed").permitAll()
                        .requestMatchers(HttpMethod.GET, "/post", "/post/{postId}", "/post/{postId}/comment").permitAll()
                        .requestMatchers(HttpMethod.GET, "/image/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/game", "/game/{gameId}", "/game/{gameId}/post").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2Login(login -> login
                        .redirectionEndpoint((endpoint) -> endpoint
                                .baseUri("/login/oauth2/*")
                        )
                        .authorizationEndpoint(endpoint -> endpoint
                                .baseUri("/oauth2/authorize")
                        )
                        .userInfoEndpoint(endpoint -> endpoint
                                .userService(oAuth2UserService)
                        )
                        .successHandler((request, response, authentication) -> {
                            if (authentication instanceof OAuth2AuthenticationToken auth2AuthenticationToken && authentication.getPrincipal() instanceof UserPrincipal principal) {
                                logger.info("User {} ({}) logged in", principal.getId(), auth2AuthenticationToken.getAuthorizedClientRegistrationId());
                            }

                            response.sendRedirect("/login/success");
                        })
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof GithubOAuth2MissingEmailException) {
                                response.sendRedirect("/login/failed?reason=github-public-email-disabled");
                                return;
                            }

                            response.sendRedirect("/login/failed?reason=unexpected-error");
                        })
                )
                .logout(configurer -> configurer
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            if (authentication instanceof OAuth2AuthenticationToken auth2AuthenticationToken && authentication.getPrincipal() instanceof UserPrincipal principal) {
                                logger.info("User {} ({}) logged out", principal.getId(), auth2AuthenticationToken.getAuthorizedClientRegistrationId());
                            }

                            response.sendRedirect("/logout/success");
                        })
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint((request, response, authException) -> {
                            if (!request.getRequestURI().equals("/ping")) {
                                logger.warn("{} - {}", request.getRequestURI(), authException.getMessage());
                            }

                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        })
                );

        return http.build();
    }

}
