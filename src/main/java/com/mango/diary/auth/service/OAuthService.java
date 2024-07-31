package com.mango.diary.auth.service;

import com.mango.diary.auth.controller.dto.SignInUriDTO;
import com.mango.diary.auth.domain.KakaoUser;
import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.domain.UserStatus;
import com.mango.diary.auth.jwt.JwtProvider;
import com.mango.diary.auth.jwt.dto.TokenResponse;
import com.mango.diary.auth.oauth.RestTemplateOAuthRequester;
import com.mango.diary.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    private final RestTemplateOAuthRequester restTemplateOAuthRequester;

    public SignInUriDTO signInUri(String redirectUri) {
        return new SignInUriDTO(restTemplateOAuthRequester.signInUri(redirectUri));
    }

    public TokenResponse signIn(KakaoUser kakaoUser) {
        if(userRepository.existsByUserEmail(kakaoUser.userEmail())){
            User user = userRepository.findByUserEmail(kakaoUser.userEmail()).get();
            return jwtProvider.createTokens(user.getId());
        }
        User user = User.builder()
                .userEmail(kakaoUser.userEmail())
                .userName(kakaoUser.userName())
                .password("kakao_user")
                .status(UserStatus.ACTIVE)
                .build();
        userRepository.save(user);

        return jwtProvider.createTokens(user.getId());
    }
}
