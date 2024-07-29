package com.mango.diary.auth.controller;

import com.mango.diary.auth.controller.dto.UserPatchRequest;
import com.mango.diary.auth.service.UserService;
import com.mango.diary.auth.support.AuthUser;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @PatchMapping("")
    public ResponseEntity<?> updateUser(@Parameter(hidden = true) @AuthUser Long userId, @RequestBody UserPatchRequest userPatchRequest) {
        userService.updateUser(userId, userPatchRequest);
        return ResponseEntity.ok().body("회원정보가 수정되었습니다.");
    }

}
