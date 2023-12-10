package com.mogumogu.spring.controller;

import com.mogumogu.spring.dto.UserDto;
import com.mogumogu.spring.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    /**
     * 가천대 이메일 인증 코드 전송
     */
    @PostMapping("/emails/verification-requests")
    public ResponseEntity<?> sendMessage(@RequestParam("email") String email) {

        log.info("----");
        authService.sendCodeToEmail(email);

        return ResponseEntity.ok().body(email + "로 메일이 전송되었습니다.");
    }

    @GetMapping("/emails/verifications")
    public ResponseEntity<?> verificationEmail(@RequestParam("email") String email,
                                               @RequestParam("code") String authCode) {
        boolean verificationResult = authService.verifiedCode(email, authCode);

        if (verificationResult) {
            return ResponseEntity.ok().body(email + " 이메일이 성공적으로 인증되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이메일 인증에 실패했습니다. 올바른 인증 코드를 입력하세요.");
        }
    }

    /**
     * 회원 가입
     */
    @PostMapping(value = "/join")
    public ResponseEntity<?> createUser(@RequestBody UserDto.UserRequestDto userRequestDto) {
        //로그
        log.info("createMember 진입");
        log.info("userRequestDto의 username : " + userRequestDto.getUsername());
        UserDto.UserResponseDto user = authService.createUser(userRequestDto);
        log.info("userResponseDto의 username : " + user.getUsername());
        return ResponseEntity.ok().body(user);
    }
}
