package com.mango.diary.auth.oauth;

import com.mango.diary.auth.controller.dto.OAuthSignInRequest;
import com.mango.diary.auth.domain.KakaoUser;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.oauth.dto.OAuthTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import com.mango.diary.auth.exception.MAuthException;

import java.net.URI;
import java.util.Map;



@RequiredArgsConstructor
@Component
@Slf4j
public class RestTemplateOAuthRequester {

    private static final String RESPONSE_TYPE = "response_type";
    private static final String CLIENT_ID = "client_id";
    private static final String REDIRECT_URI = "redirect_uri";
    private static final String GRANT_TYPE = "grant_type";
    private static final String SCOPE = "scope";
    private static final String CODE = "code";

    @Value("${oauth2.provider.kakao.login-uri}")
    private String KAKAO_LOGIN_URI;
    @Value("${oauth2.provider.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth2.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;
    @Value("${oauth2.provider.kakao.info-uri}")
    private String KAKAO_INFO_URI;
    @Value("${oauth2.provider.kakao.scope}")
    private String KAKAO_SCOPE;

    private final RestTemplate restTemplate;

    public String signInUri(String redirectUri) {
        return UriComponentsBuilder.fromUriString(KAKAO_LOGIN_URI)
                .queryParam(CLIENT_ID, KAKAO_CLIENT_ID)
                .queryParam(REDIRECT_URI, redirectUri)
                .queryParam(RESPONSE_TYPE, CODE)
                .build()
                .toString();
    }

    public KakaoUser signIn(OAuthSignInRequest request) {
        OAuthTokenResponse accessToken = getKakaoToken(request);
        KakaoUser user = getKakaoUserInfo(accessToken);

        return user;
    }

    private KakaoUser getKakaoUserInfo(OAuthTokenResponse accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken.accessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(SCOPE, KAKAO_SCOPE);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        URI infoUri = URI.create(KAKAO_INFO_URI);
        RequestEntity<?> requestEntity = new RequestEntity<>(headers, HttpMethod.GET, infoUri);

        Map<String, Object> response = requestUserAttributes(requestEntity);
        log.info("response: {}", response);

        return new KakaoUser(response);
    }

    private Map<String, Object> requestUserAttributes(RequestEntity<?> requestEntity) {
        try {
            ResponseEntity<Map<String, Object>> responseEntity = restTemplate.exchange(requestEntity, new ParameterizedTypeReference<>() {});
            return responseEntity.getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            switch (statusCode) {
                case UNAUTHORIZED:
                    throw new MAuthException(MAuthErrorCode.KAKAO_UNAUTHORIZED);
                case FORBIDDEN:
                    throw new MAuthException(MAuthErrorCode.KAKAO_FORBIDDEN);
                case NOT_FOUND:
                    throw new MAuthException(MAuthErrorCode.KAKAO_NOT_FOUND);
                case BAD_REQUEST:
                    throw new MAuthException(MAuthErrorCode.KAKAO_BAD_REQUEST);
                default:
                    throw new MAuthException(MAuthErrorCode.KAKAO_INTERNAL_SERVER_ERROR);
            }
        } catch (ResourceAccessException e) {
            throw new MAuthException(MAuthErrorCode.KAKAO_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new MAuthException(MAuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private OAuthTokenResponse getKakaoToken(OAuthSignInRequest signInRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(headers);
        URI tokenUri = getTokenUri(signInRequest);

        return getoAuthTokenResponse(tokenUri, request);
    }

    private OAuthTokenResponse getoAuthTokenResponse(URI tokenUri, HttpEntity<MultiValueMap<String, String>> request) {
        try {
            return restTemplate.postForEntity(tokenUri, request, OAuthTokenResponse.class).getBody();
        } catch (HttpClientErrorException e) {
            HttpStatus statusCode = (HttpStatus) e.getStatusCode();
            switch (statusCode) {
                case UNAUTHORIZED:
                    throw new MAuthException(MAuthErrorCode.KAKAO_UNAUTHORIZED);
                case FORBIDDEN:
                    throw new MAuthException(MAuthErrorCode.KAKAO_FORBIDDEN);
                case NOT_FOUND:
                    throw new MAuthException(MAuthErrorCode.KAKAO_NOT_FOUND);
                case BAD_REQUEST:
                    throw new MAuthException(MAuthErrorCode.KAKAO_BAD_REQUEST);
                default:
                    throw new MAuthException(MAuthErrorCode.KAKAO_INTERNAL_SERVER_ERROR);
            }
        } catch (ResourceAccessException e) {
            throw new MAuthException(MAuthErrorCode.KAKAO_INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            throw new MAuthException(MAuthErrorCode.INTERNAL_SERVER_ERROR);
        }
    }


    private URI getTokenUri(OAuthSignInRequest request) {
        return UriComponentsBuilder.fromUriString(KAKAO_TOKEN_URI)
                .queryParam(CODE, request.code())
                .queryParam(GRANT_TYPE, "authorization_code")
                .queryParam(REDIRECT_URI, request.redirectUri())
                .queryParam(CLIENT_ID, KAKAO_CLIENT_ID)
                .build()
                .toUri();
    }
}
