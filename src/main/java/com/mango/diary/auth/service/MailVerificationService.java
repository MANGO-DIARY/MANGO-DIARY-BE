package com.mango.diary.auth.service;

import com.mango.diary.auth.controller.dto.SendMailDTO;
import com.mango.diary.auth.controller.dto.VerificationPurpose;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.repository.UserRepository;
import com.mango.diary.common.redis.RedisDao;
import com.mango.diary.auth.exception.MAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailVerificationService {

    private final UserRepository userRepository;
    private final JavaMailSender javaMailSender;
    private final RedisDao redisDao;

    public void sendVerificationEmail(SendMailDTO sendMailDTO) {
        String userEmail = sendMailDTO.userEmail();
        VerificationPurpose purpose = sendMailDTO.purpose();
        switch (purpose){
            case SIGN_UP:
                if (userRepository.existsByUserEmail(userEmail)) {
                    throw new MAuthException(MAuthErrorCode.EMAIL_ALREADY_EXISTS);
                }
                break;
            case RESET_PASSWORD:
                if (!userRepository.existsByUserEmail(userEmail)) {
                    throw new MAuthException(MAuthErrorCode.EMAIL_NOT_FOUND);
                }
                break;
        }

        String verificationCode = generateVerificationCode();
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("아프지망고 인증 코드입니다");
        message.setText("인증 코드 : " + verificationCode);
        javaMailSender.send(message);

        redisDao.setVerificationCode(userEmail, verificationCode, 3L);
    }

    public void verifyCode(String userEmail, String code) {
        String verificationCode = redisDao.getVerificationCode(userEmail);
        if(!code.equals(verificationCode)){
            throw new MAuthException(MAuthErrorCode.INVALID_VERIFICATION_CODE);
        }
        redisDao.setVerification(userEmail, 3L);
    }


    private String generateVerificationCode() {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        return String.valueOf(code);
    }
}
