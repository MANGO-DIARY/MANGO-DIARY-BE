package com.mango.diary.auth.interceptor;

import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.support.AuthenticationExtractor;
import com.mango.diary.common.redis.RedisDao;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.mango.diary.auth.exception.MAuthException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TokenBlackListInterceptor implements HandlerInterceptor{
    private final RedisDao redisDao;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        Optional<String> accessToken = AuthenticationExtractor.extractAccessToken(request);
        if (accessToken.isPresent() && redisDao.isKeyOfAccessTokenInBlackList(accessToken.get())) {
            throw new MAuthException(MAuthErrorCode.ALREADY_SIGN_OUT);
        }
        return true;
    }
}