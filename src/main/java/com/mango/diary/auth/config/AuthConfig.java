package com.mango.diary.auth.config;

import com.mango.diary.auth.interceptor.SelectiveApiInterceptor;
import com.mango.diary.auth.interceptor.SignInInterceptor;
import com.mango.diary.auth.interceptor.TokenBlackListInterceptor;
import com.mango.diary.auth.support.AuthArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

import static com.mango.diary.auth.interceptor.HttpMethod.*;

@Configuration
@RequiredArgsConstructor
public class AuthConfig implements WebMvcConfigurer {
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private final AuthArgumentResolver authArgumentResolver;
    private final SignInInterceptor signInInterceptor;
    private final TokenBlackListInterceptor tokenBlackListInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenBlackListInterceptor())
                .order(1);
        registry.addInterceptor(signInInterceptor())
                .order(2);
    }

    private HandlerInterceptor tokenBlackListInterceptor() {
        return new SelectiveApiInterceptor(tokenBlackListInterceptor)
                .addIncludePattern("/api/v1/auth/sign-out", DELETE)
                .addIncludePattern("/api/v1/diary/**",POST)
                .addIncludePattern("/api/v1/diary/**",GET)
                .addIncludePattern("/api/v1/diary/**",DELETE)
                ;
    }

    private HandlerInterceptor signInInterceptor() {
        return new SelectiveApiInterceptor(signInInterceptor)
                .addIncludePattern("/api/v1/auth/sign-out", DELETE)
                .addIncludePattern("/api/v1/diary/**",POST)
                .addIncludePattern("/api/v1/diary/**",GET)
                .addIncludePattern("/api/v1/diary/**",DELETE)
                ;
    }


    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authArgumentResolver);
    }
}
