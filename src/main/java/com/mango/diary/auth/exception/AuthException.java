package com.mango.diary.auth.exception;

import com.mango.diary.common.exception.BaseException;


public class AuthException extends BaseException {

        public AuthException(AuthErrorCode authErrorCode) {
            super(authErrorCode);
        }
}
