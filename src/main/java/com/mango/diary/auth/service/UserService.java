package com.mango.diary.auth.service;

import com.mango.diary.auth.controller.dto.UserPatchRequest;
import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.exception.AuthErrorCode;
import com.mango.diary.auth.exception.AuthException;
import com.mango.diary.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUser(Long userId, UserPatchRequest userPatchRequest) {
        User user = userRepository.findById(userId).orElseThrow(()->new AuthException(AuthErrorCode.USER_NOT_FOUND));

        if (Objects.equals(userPatchRequest.userName(), user.getUserName())){
           throw new AuthException(AuthErrorCode.INVALID_INPUT);
        }

        user.setUserName(userPatchRequest.userName());
    }
}
