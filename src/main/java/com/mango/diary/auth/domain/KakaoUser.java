package com.mango.diary.auth.domain;

import java.util.Map;

public class KakaoUser implements OAuthUser {

    private Long id;
    private String userName;

    public KakaoUser(Map<String, Object> attributes) {
        this.id = (Long) attributes.get("id");
        this.userName = (String) attributes.get("nickname");
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public String userName() {
        return userName;
    }

}
