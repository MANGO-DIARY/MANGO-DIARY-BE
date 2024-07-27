package com.mango.diary.auth.controller;


import com.mango.diary.auth.controller.dto.OAuthSignInRequest;
import com.mango.diary.auth.controller.dto.SignInUriDTO;
import com.mango.diary.auth.domain.KakaoUser;
import com.mango.diary.auth.jwt.dto.TokenResponse;
import com.mango.diary.auth.oauth.RestTemplateOAuthRequester;
import com.mango.diary.auth.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/oauth")
public class OAuthController {

    private final OAuthService oAuthService;
    private final RestTemplateOAuthRequester restTemplateOAuthRequester;

    @GetMapping("/kakao/sign-in-uri")
    public ResponseEntity<SignInUriDTO> signInUri(
            @RequestParam("redirect-uri") String redirectUri
    ) {
        return ResponseEntity.ok(oAuthService.signInUri(redirectUri));
    }

    @PostMapping("/kakao/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody OAuthSignInRequest request) {
        KakaoUser kakaoUser = restTemplateOAuthRequester.signIn(request);
        return ResponseEntity.ok(oAuthService.signIn(kakaoUser));
    }
}
