package com.mango.diary.auth.support;


import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.exception.MAuthException;
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
            throw new MAuthException(MAuthErrorCode.UNAUTHORIZED);
        }
        return userId;
    }

    public void setNotAuthenticated() {
        this.userId = ANONYMOUS_USER;
    }
}
