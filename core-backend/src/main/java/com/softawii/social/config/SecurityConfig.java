package com.softawii.social.config;

import com.softawii.social.exception.GithubOAuth2MissingEmailException;
import com.softawii.social.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${softawii.front.url}")
    private String frontUrl;


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
                .cors(configurer -> configurer.configurationSource(corsConfigurationSource()))
                .formLogin(configurer -> configurer
                        .loginPage("/")
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/favicon.ico", "/logout/success", "/login/failed").permitAll()
                        .requestMatchers(HttpMethod.GET, "/post", "/post/{postId}", "/post/{postId}/comment", "/image/{id}", "/game").permitAll()
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
                        .defaultSuccessUrl("/login/success", true)
                        .failureHandler((request, response, exception) -> {
                            if (exception instanceof GithubOAuth2MissingEmailException) {
                                response.sendRedirect("/login/failed?reason=github-public-email-disabled");
                                return;
                            }
                            response.sendError(HttpServletResponse.SC_FORBIDDEN);
                        })
                )
                .logout(configurer -> configurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/logout/success")
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println(authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        })
                )
                .headers(configurer -> configurer
                        .frameOptions(frameOptionsConfig -> frameOptionsConfig
                                .sameOrigin()
                        )
                )
        ;

//        DefaultLoginPageConfigurer defaultLoginPageConfigurer = http.removeConfigurer(DefaultLoginPageConfigurer.class);

        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontUrl));
        configuration.setAllowCredentials(true);
        configuration.addAllowedMethod("*");
        configuration.addExposedHeader("*");
        configuration.addAllowedHeader("*");
        configuration.validateAllowCredentials();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
