package com.softawii.social.exception;

public class GithubOAuth2MissingEmailException extends OAuth2AuthenticationProcessingException {

    private final String name;

    public GithubOAuth2MissingEmailException(String msg, String name, Throwable t) {
        super(msg, t);
        this.name = name;
    }

    public GithubOAuth2MissingEmailException(String msg, String name) {
        super(msg);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
