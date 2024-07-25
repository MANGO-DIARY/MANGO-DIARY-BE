package com.mango.diary.auth.interceptor;

import com.mango.diary.auth.exception.AuthErrorCode;
import com.mango.diary.auth.jwt.JwtProvider;
import com.mango.diary.auth.support.AuthenticationContext;
import com.mango.diary.auth.support.AuthenticationExtractor;
import com.mango.diary.auth.exception.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class SignInInterceptor implements HandlerInterceptor {

    private final JwtProvider jwtProvider;
    private final AuthenticationContext authenticationContext;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String accessToken = AuthenticationExtractor.extractAccessToken(request)
                .orElseThrow(() -> new AuthException(AuthErrorCode.UNAUTHORIZED));

        Long userId = jwtProvider.extractIdFromAccessToken(accessToken);
        authenticationContext.setAuthentication(userId);

        return true;
    }
}
