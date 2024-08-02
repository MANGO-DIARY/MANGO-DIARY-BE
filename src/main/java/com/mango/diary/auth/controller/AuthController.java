package com.mango.diary.auth.controller;

import com.mango.diary.auth.controller.dto.ReissueTokenResponse;
import com.mango.diary.auth.controller.dto.SignUpRequestDTO;
import com.mango.diary.auth.controller.dto.TokenReissueDTO;
import com.mango.diary.auth.controller.dto.UserDTO;
import com.mango.diary.auth.jwt.dto.TokenResponse;
import com.mango.diary.auth.service.AuthService;
import com.mango.diary.auth.support.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    @Operation(summary = "회원가입", description = "회원가입을 수행하기 전에 이메일 인증을 먼저 진행해야합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "회원가입이 완료되었습니다."),
                    @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않습니다."),
                    @ApiResponse(responseCode = "400", description = "이메일 인증이 완료되지 않았습니다."),
                    @ApiResponse(responseCode = "409", description = "이미 가입된 이메일입니다.")
    })
    public ResponseEntity<?> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        authService.signUp(signUpRequestDTO);
        return ResponseEntity.ok().body("회원가입이 완료되었습니다.");
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그인 되었습니다."),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    public ResponseEntity<TokenResponse> signIn(@RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(authService.signIn(userDTO));
    }


    @DeleteMapping("/sign-out")
    @Operation(summary = "로그아웃", description = "로그아웃을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "로그아웃 되었습니다."),
                    @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자입니다.")
    })
    public ResponseEntity<?> signOut(@Parameter(hidden = true) @AuthUser Long userId,
                                     HttpServletRequest request) {
        authService.signOut(userId, request);
        return ResponseEntity.ok().body("로그아웃 되었습니다.");
    }


    @PostMapping("/reset-pw")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호 재설정을 수행합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "비밀번호가 재설정되었습니다."),
                    @ApiResponse(responseCode = "400", description = "이메일 인증이 완료되지 않았습니다."),
                    @ApiResponse(responseCode = "404", description = "존재하지 않는 회원 이메일입니다.")
    })
    public ResponseEntity<?> resetPassword(@RequestBody UserDTO userDTO) {
        authService.resetPassword(userDTO);
        return ResponseEntity.ok().body("비밀번호가 재설정되었습니다.");
    }

    @PostMapping("/token-reissue")
    @Operation(summary = "토큰 재발급", description = "토큰을 재발급합니다.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "토큰이 재발급되었습니다."),
                    @ApiResponse(responseCode = "400", description = "입력값이 올바르지 않습니다."),
                    @ApiResponse(responseCode = "404", description = "해당 유저에 대한 서버에 저장된 리프레시 토큰을 찾을 수 없습니다."),
                    @ApiResponse(responseCode = "400", description = "리프레시 토큰이 유효하지 않습니다.")
    })
    public ResponseEntity<ReissueTokenResponse> reissueToken(TokenReissueDTO tokenReissueDTO) {
        return ResponseEntity.ok(authService.reissueToken(tokenReissueDTO));
    }
}
