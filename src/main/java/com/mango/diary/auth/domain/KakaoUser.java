package com.mango.diary.auth.domain;

import java.util.Map;

public class KakaoUser implements OAuthUser {

    private String userEmail;
    private String userName;

    public KakaoUser(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        String email = (String) kakaoAccount.get("email");

        if (email != null) {
            this.userEmail = email;
        } else {
            this.userEmail = attributes.get("id").toString();
        }

        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
        String nickname = (String) profile.get("nickname");
        
        if (nickname != null) {
            this.userName = (String) profile.get("nickname");
        } else {
            this.userName = "아프지망고";
        }
    }
    @Override
    public String userEmail() {
        return userEmail;
    }

    @Override
    public String userName() {
        return userName;
    }

}
