package com.mango.diary.auth.exception;

import com.mango.diary.common.exception.BaseException;

public class MAuthException extends BaseException {

        public MAuthException(MAuthErrorCode MAuthErrorCode) {
            super(MAuthErrorCode);
        }
}
