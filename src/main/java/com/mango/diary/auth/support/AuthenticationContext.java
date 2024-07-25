package com.mango.diary.auth.support;


import com.mango.diary.auth.exception.AuthErrorCode;
import com.mango.diary.auth.exception.AuthException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.util.Objects;

@RequestScope
@Component
public class AuthenticationContext {
    private static final Long ANONYMOUS_USER = -1L;
    private Long userId;

    public void setAuthentication(Long userId) {
        this.userId = userId;
    }

    public Long getAuthentication(){
        if(Objects.isNull(this.userId)){
            throw new AuthException(AuthErrorCode.UNAUTHORIZED);
        }
        return userId;
    }

    public void setNotAuthenticated() {
        this.userId = ANONYMOUS_USER;
    }
}
