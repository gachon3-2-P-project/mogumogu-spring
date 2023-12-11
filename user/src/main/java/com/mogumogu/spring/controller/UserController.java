package com.mogumogu.spring.controller;

import com.mogumogu.spring.dto.UserDto;
import com.mogumogu.spring.service.JoinService;
import com.mogumogu.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final JoinService joinService;

  /**
   * 이메일 인증 요청
   * @param email
   * @param authCode
   * @return
   */
  @GetMapping("/emails/verifications")
  public ResponseEntity<?> verificationEmail(@RequestParam("email") String email,
                                             @RequestParam("code") String authCode) {
    boolean verificationResult = joinService.verifiedCode(email, authCode);

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

    String email = userRequestDto.getUsername();
    joinService.sendCodeToEmail(email);
    log.info("createMember 진입");
    log.info("userRequestDto의 username : " + userRequestDto.getUsername());
    UserDto.UserResponseDto user = joinService.createUser(userRequestDto);
    log.info("userResponseDto의 username : " + user.getUsername());

    return ResponseEntity.ok().body(email + "로 메일이 전송되었습니다.");
  }

  /**
   * 유저 고유 id로 조회
   */
  @GetMapping("/get")
  public ResponseEntity<?> getUser (@RequestParam("userId") Long userId) {

    UserDto.UserResponseDto user = userService.getUser(userId);
    return ResponseEntity.ok().body(user);
  }

  /**
   *
   * update -> 닉네임 수정
   */
  @PatchMapping("/update")
  public ResponseEntity<?> updateUser(@RequestParam("userId") Long userId, @RequestBody UserDto.UserPatchDto userPatchDto) {
    return ResponseEntity.ok().body(userService.updateUser(userId, userPatchDto));
  }

  /**
   * 사용자 탈퇴 기능
   */
  @DeleteMapping("/delete")
  public ResponseEntity<?> deleteUser(@RequestParam("userId") long userId) {
    userService.deleteUser(userId);
    return ResponseEntity.ok().body("Deleted UserId: " + userId);
  }

  /**
   * 관리자 사용자 전체 조회
   */
  @GetMapping("/admin/getAll")
  public ResponseEntity<List<UserDto.UserResponseDto>> getAllUser() {
    List<UserDto.UserResponseDto> users = userService.getAllUser();
    return ResponseEntity.ok().body(users);
  }

}
