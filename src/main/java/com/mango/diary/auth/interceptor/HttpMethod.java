package com.mango.diary.auth.interceptor;

public enum HttpMethod {
    GET,
    POST,
    PUT,
    DELETE,
    PATCH,
    OPTIONS,
    HEAD,
    TRACE,
    CONNECT,
    ANY,
    ;

    public boolean matches(String method) {
        return this == ANY || this.name().equalsIgnoreCase(method);
    }
}
