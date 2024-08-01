package com.mango.diary.auth.service;

import com.mango.diary.auth.controller.dto.ReissueTokenResponse;
import com.mango.diary.auth.controller.dto.SignUpRequestDTO;
import com.mango.diary.auth.controller.dto.TokenReissueDTO;
import com.mango.diary.auth.controller.dto.UserDTO;
import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.domain.UserStatus;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.exception.MAuthException;
import com.mango.diary.auth.jwt.JwtProvider;
import com.mango.diary.auth.jwt.dto.TokenResponse;
import com.mango.diary.auth.repository.UserRepository;
import com.mango.diary.auth.support.AuthenticationExtractor;
import com.mango.diary.common.redis.RedisDao;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RedisDao redisDao;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtProvider jwtProvider;
    public void signUp(SignUpRequestDTO signUpRequestDTO) {

        String userEmail = signUpRequestDTO.userEmail();
        String password = signUpRequestDTO.password();
        String userName = signUpRequestDTO.userName();

        if(userEmail == null || password == null || userName == null){
            throw new MAuthException(MAuthErrorCode.INVALID_INPUT);
        }

        if(userRepository.existsByUserEmail(userEmail)){
            throw new MAuthException(MAuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (!(redisDao.getVerification(userEmail).equals("Verified"))) {
            throw new MAuthException(MAuthErrorCode.VERIFICATION_INCOMPLETE);
        }

        User user = User.builder()
                .userEmail(userEmail)
                .password(bCryptPasswordEncoder.encode(password))
                .userName(userName)
                .status(UserStatus.ACTIVE)
                .build();

        userRepository.save(user);
    }

    public TokenResponse signIn(UserDTO userDTO) {
        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new MAuthException(MAuthErrorCode.INCORRECT_INPUT));

        //탈퇴 기능 구현할거면 회원 상태 검증 추가

        if(!bCryptPasswordEncoder.matches(password, user.getPassword())){
            throw new MAuthException(MAuthErrorCode.INCORRECT_INPUT);
        }

        return jwtProvider.createTokens(user.getId());
    }

    public void signOut(Long userId, HttpServletRequest request) {
        String accessToken = AuthenticationExtractor.extractAccessToken(request)
                .orElseThrow(() -> new MAuthException(MAuthErrorCode.UNAUTHORIZED));

        redisDao.deleteRefreshToken(userId.toString());
        redisDao.setAccessTokenSignOut(accessToken);
    }

    public void resetPassword(UserDTO userDTO) {
        String userEmail = userDTO.userEmail();
        String password = userDTO.password();

        if(!(redisDao.getVerification(userEmail).equals("Verified"))){
            throw new MAuthException(MAuthErrorCode.VERIFICATION_INCOMPLETE);
        }
        redisDao.deleteVerification(userEmail);

        User user = userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new MAuthException(MAuthErrorCode.EMAIL_NOT_FOUND));
        user.setPassword(bCryptPasswordEncoder.encode(password));

        redisDao.deleteRefreshToken(user.getId().toString());
    }

    public ReissueTokenResponse reissueToken(TokenReissueDTO tokenReissueDTO) {
        if(tokenReissueDTO.refreshToken() == null){
            throw new MAuthException(MAuthErrorCode.INVALID_INPUT);
        }

        Long userId = jwtProvider.extractIdFromRefreshToken(tokenReissueDTO.refreshToken());

        String refreshToken = redisDao.getRefreshToken(userId.toString());

        if (refreshToken == null) {
            throw new MAuthException(MAuthErrorCode.REFRESH_TOKEN_NOT_FOUND);
        }

        refreshToken = refreshToken.replaceAll("^\"|\"$", "");

        if(!refreshToken.equals(tokenReissueDTO.refreshToken())){
            throw new MAuthException(MAuthErrorCode.INVALID_REFRESH_TOKEN);
        }

        return jwtProvider.reissueTokens(userId);
    }
}
