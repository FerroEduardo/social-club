package com.softawii.social.config;

import com.softawii.social.service.OAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private OAuth2UserService oAuth2UserService;

    public SecurityConfig(OAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(configurer -> configurer
                        .disable()
                )
                .cors(configurer -> configurer
                        .disable()
                )
                .httpBasic(configurer -> configurer
                        .disable()
                )
                .formLogin(configurer -> configurer
                        .loginPage("/")
                )
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers("/", "/favicon.ico").permitAll()
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
                        .defaultSuccessUrl("/user/me", true)
                )
                .logout(configurer -> configurer
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/?logout=success")
                )
                .exceptionHandling(configurer -> configurer
                        .authenticationEntryPoint((request, response, authException) -> {
                            System.out.println(authException.getMessage());
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
                        })
                )
        ;

//        DefaultLoginPageConfigurer defaultLoginPageConfigurer = http.removeConfigurer(DefaultLoginPageConfigurer.class);

        return http.build();
    }

}
