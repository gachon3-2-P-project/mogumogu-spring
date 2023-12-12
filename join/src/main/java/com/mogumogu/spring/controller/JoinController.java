package com.mogumogu.spring.controller;

import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.dto.UserDto;
import com.mogumogu.spring.exception.BusinessLogicException;
import com.mogumogu.spring.exception.ExceptionCode;
import com.mogumogu.spring.repository.EmailRepository;
import com.mogumogu.spring.repository.UserRepository;
import com.mogumogu.spring.service.JoinService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class JoinController {
    private final JoinService joinService;
    private final EmailRepository emailRepository;
    private final UserRepository userRepository;

    /**
     * 이메일 인증 요청
     * @param email
     * @param authCode
     * @return
     */
    @GetMapping("/emails/verifications")
    @Transactional
    public ResponseEntity<?> verificationEmail(@RequestParam("email") String email,
                                               @RequestParam("code") String authCode) {
        boolean verificationResult = joinService.verifiedCode(email, authCode);

        if (verificationResult) {

            UserEntity user = userRepository.findByUsername(email);

            Long userId = user.getId();

            // 이메일 인증 성공 시 EmailAuth테이블의 해당 데이터 삭제 -> user테이블에는 데이터 존재
            emailRepository.deleteByEmail(email);
            return ResponseEntity.ok().body(email + " 이메일이 성공적으로 인증되었습니다." + "userId : " + userId);
        } else {
            return ResponseEntity.badRequest().body("이메일 인증에 실패했습니다. 올바른 인증 코드를 입력하세요.");
        }
    }

    /**
     * 회원 가입
     */
    @PostMapping(value = "/process")
    public ResponseEntity<?> createUser(@RequestBody UserDto.UserRequestDto userRequestDto) {
        //로그

        String email = userRequestDto.getUsername();
        joinService.sendCodeToEmail(email);
        log.info("createMember 진입");
        log.info("userRequestDto의 username : " + userRequestDto.getUsername());
        UserDto.UserResponseDto user = joinService.createUser(userRequestDto);
        log.info("userResponseDto의 username : " + user.getUsername());

        return ResponseEntity.ok().body(email + "로 메일이 전송되었습니다.");
    }
}
