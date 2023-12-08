package com.mogumogu.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class UserController {

  @GetMapping("/test")
  public String test() {
    log.info("User - Test()");
    return "User - Test()";
  }

}
