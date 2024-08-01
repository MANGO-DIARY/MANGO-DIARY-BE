package com.mango.diary.auth.service;

import com.mango.diary.auth.controller.dto.UserPatchRequest;
import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.exception.MAuthException;
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
        User user = userRepository.findById(userId).orElseThrow(()->new MAuthException(MAuthErrorCode.USER_NOT_FOUND));

        if (Objects.equals(userPatchRequest.userName(), user.getUserName())){
           throw new MAuthException(MAuthErrorCode.INVALID_INPUT);
        }

        user.setUserName(userPatchRequest.userName());
    }
}
