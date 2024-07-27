package com.mango.diary.auth.controller.dto;

public record OAuthSignInRequest(
        String redirectUri,
        String code
) {
}
