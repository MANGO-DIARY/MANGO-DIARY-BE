package com.mango.diary.auth.interceptor;

import org.springframework.util.PathMatcher;

public record PathPattern(
        String path,
        HttpMethod method
){
    public boolean matches(PathMatcher pathMatcher, String targetPath, String pathMethod) {
        return pathMatcher.match(path, targetPath) && method.matches(pathMethod);
    }
}
