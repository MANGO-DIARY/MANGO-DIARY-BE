package com.mango.diary.auth.jwt;


import com.mango.diary.auth.controller.dto.ReissueTokenResponse;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.exception.MAuthException;
import com.mango.diary.auth.jwt.dto.TokenResponse;
import com.mango.diary.common.redis.RedisDao;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@RequiredArgsConstructor
@Getter
@Component
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    private final RedisDao redisDao;

    private static final Long ONE_HOUR = 60 * 60L;
    private static final Long ONE_DAY = 60 * 60L * 24;
    private static final Long ONE_MONTH = 60 * 60L * 24 * 30;
    private static final Long ACCESS_TOKEN_EXPIRATION_TIME = ONE_HOUR * 1L;
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = ONE_MONTH * 6L;


    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public TokenResponse createTokens(Long userId) {
        String accessToken = createAccessToken(userId);
        String refreshToken = createRefreshToken(userId);

        return new TokenResponse(accessToken, refreshToken);
    }

    private String createAccessToken(Long userId) {
        Claims claims = Jwts.claims();
        claims.put("id", userId);
        claims.put("type", "access");
        return accessToken(claims);
    }

    private String createRefreshToken(Long userId) {
        Claims claims = Jwts.claims();
        claims.put("id", userId);
        claims.put("type", "refresh");
        String refreshToken = refreshToken(claims);
        redisDao.setRefreshToken(userId.toString(), refreshToken, REFRESH_TOKEN_EXPIRATION_TIME);
        return refreshToken(claims);
    }

    private String accessToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt())
                .setExpiration(accessTokenExpiredAt())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private String refreshToken(Claims claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(issuedAt())
                .setExpiration(refreshTokenExpiredAt())
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    private Date issuedAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
    }

    private Date accessTokenExpiredAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.plusSeconds(ACCESS_TOKEN_EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant());

    }

    private Date refreshTokenExpiredAt() {
        LocalDateTime now = LocalDateTime.now();
        return Date.from(now.plusSeconds(REFRESH_TOKEN_EXPIRATION_TIME).atZone(ZoneId.systemDefault()).toInstant());
    }

    public Long extractIdFromAccessToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            String type = claims.get("type", String.class);
            if (!type.equals("access")) {
                throw new MAuthException(MAuthErrorCode.INVALID_TOKEN_TYPE);
            }
            return claims.get("id", Long.class);
        } catch (ExpiredJwtException e) {
            throw new MAuthException(MAuthErrorCode.EXPIRED_TOKEN);
        } catch (SecurityException e) {
            throw new MAuthException(MAuthErrorCode.SECURITY_ERROR);
        } catch (MalformedJwtException e) {
            throw new MAuthException(MAuthErrorCode.MALFORMED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new MAuthException(MAuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new MAuthException(MAuthErrorCode.INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new MAuthException(MAuthErrorCode.INVALID_TOKEN_FORMAT);
        } catch (JwtException e) {
            //위애서 안걸린 jwt 기타 익셉션
            throw new MAuthException(MAuthErrorCode.JWT_ERROR);
        }
    }

    public Long extractIdFromRefreshToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            String type = claims.get("type", String.class);
            if (!type.equals("refresh")) {
                throw new MAuthException(MAuthErrorCode.INVALID_TOKEN_TYPE);
            }
            return claims.get("id", Long.class);
        } catch (ExpiredJwtException e) {
            throw new MAuthException(MAuthErrorCode.EXPIRED_TOKEN);
        } catch (SecurityException e) {
            throw new MAuthException(MAuthErrorCode.SECURITY_ERROR);
        } catch (MalformedJwtException e) {
            throw new MAuthException(MAuthErrorCode.MALFORMED_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new MAuthException(MAuthErrorCode.UNSUPPORTED_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new MAuthException(MAuthErrorCode.INVALID_TOKEN);
        } catch (SignatureException e) {
            throw new MAuthException(MAuthErrorCode.INVALID_TOKEN_FORMAT);
        } catch (JwtException e) {
            //위애서 안걸린 jwt 기타 익셉션
            throw new MAuthException(MAuthErrorCode.JWT_ERROR);
        }
    }

    public ReissueTokenResponse reissueTokens(Long userId) {
        String accessToken = createAccessToken(userId);
        return new ReissueTokenResponse(accessToken);
    }
}
