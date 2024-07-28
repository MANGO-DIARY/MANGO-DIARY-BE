package com.mango.diary.auth.oauth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthTokenResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("expires_in") Long expiresIn,
        String scope,
        @JsonProperty("token_type") String tokenType
) {
}
