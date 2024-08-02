package com.mango.diary.auth.service;

import com.mango.diary.auth.controller.dto.UserPatchRequest;
import com.mango.diary.auth.domain.User;
import com.mango.diary.auth.exception.MAuthErrorCode;
import com.mango.diary.auth.exception.MAuthException;
import com.mango.diary.auth.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void updateUser(Long userId, UserPatchRequest userPatchRequest) {
        User user = userRepository.findById(userId).orElseThrow(()->new MAuthException(MAuthErrorCode.USER_NOT_FOUND));

        if (userPatchRequest.userName().equals(user.getUserName())){
           throw new MAuthException(MAuthErrorCode.SAME_AS_PREVIOUS);
        }

        user.setUserName(userPatchRequest.userName());
    }
}
