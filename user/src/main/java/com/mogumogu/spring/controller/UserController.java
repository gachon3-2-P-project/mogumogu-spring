package com.mogumogu.spring.controller;

import com.mogumogu.spring.dto.ArticleDto;
import com.mogumogu.spring.dto.UserDto;
import com.mogumogu.spring.exception.BusinessLogicException;
import com.mogumogu.spring.exception.ExceptionCode;
import com.mogumogu.spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;

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
    try {
      UserDto.UserResponseDto updatedUser = userService.updateUser(userId, userPatchDto);
      return ResponseEntity.ok().body(updatedUser);
    } catch (BusinessLogicException e) {

      return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
  }

  /**
   * 유저 고유 id로 조회
   */
  @GetMapping("/getUserArticles")
  public ResponseEntity<?> getUserArticles(@RequestParam("userId") Long userId) {
    List<ArticleDto.ArticleResponseDto> articles = userService.getUserArticles(userId);
    return ResponseEntity.ok().body(articles);
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

  /**
   * logout -> 시큐리티에서 처리
   */
  @GetMapping("/logout")
  public RedirectView redirectHandler() {
    log.info("로그아웃 시작");
    RedirectView redirectView = new RedirectView();
    redirectView.setUrl("http://dana-seo.shop:3000");
    return redirectView;
  }

}
