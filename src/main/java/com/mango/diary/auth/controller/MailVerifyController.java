package com.mango.diary.auth.controller;

import com.mango.diary.auth.controller.dto.SendMailDTO;
import com.mango.diary.auth.controller.dto.VerifyMailDTO;
import com.mango.diary.auth.service.MailVerificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/mail")
public class MailVerifyController {
    private final MailVerificationService mailVerificationService;
    @PostMapping("/send")
    public ResponseEntity<?> sendVerificationEmail(@RequestBody SendMailDTO sendMailDTO) {
        mailVerificationService.sendVerificationEmail(sendMailDTO);
        return ResponseEntity.ok().body(sendMailDTO.userEmail() + "로 인증번호가 전송 되었습니다.");
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyMailDTO verifyMailDTO) {
        mailVerificationService.verifyCode(verifyMailDTO.userEmail(), verifyMailDTO.code());
        return ResponseEntity.ok().body("인증이 완료되었습니다.");
    }
}
