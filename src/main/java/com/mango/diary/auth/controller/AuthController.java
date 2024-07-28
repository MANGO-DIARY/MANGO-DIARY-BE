package com.mango.diary.auth.controller;

import com.mango.diary.auth.controller.dto.ReissueTokenResponse;
import com.mango.diary.auth.controller.dto.SignUpRequestDTO;
import com.mango.diary.auth.controller.dto.TokenReissueDTO;
import com.mango.diary.auth.controller.dto.UserDTO;
import com.mango.diary.auth.jwt.dto.TokenResponse;
import com.mango.diary.auth.service.AuthService;
import com.mango.diary.auth.support.AuthUser;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;
    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/sign-in")
    public ResponseEntity<TokenResponse> signIn(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.signIn(userDTO));
    }

    @DeleteMapping("/sign-out")
    public ResponseEntity<?> signOut(@Parameter(hidden = true) @AuthUser Long userId,
                                     HttpServletRequest request) {
        authService.signOut(userId, request);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }

    @PostMapping("/reset-pw")
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO) {
        authService.resetPassword(userDTO);
        return ResponseEntity.ok().body("비밀번호가 재설정되었습니다.");
    }

    @PostMapping("/token-reissue")
    public ResponseEntity<ReissueTokenResponse> reissueToken(TokenReissueDTO tokenReissueDTO) {
        return ResponseEntity.ok(authService.reissueToken(tokenReissueDTO));
    }
}
