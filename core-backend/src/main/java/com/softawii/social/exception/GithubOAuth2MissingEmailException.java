package com.softawii.social.exception;

public class GithubOAuth2MissingEmailException extends OAuth2AuthenticationProcessingException{
    public GithubOAuth2MissingEmailException(String msg, Throwable t) {
        super(msg, t);
    }

    public GithubOAuth2MissingEmailException(String msg) {
        super(msg);
    }
}
