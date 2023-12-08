package com.mogumogu.message.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class ApiController {

  @GetMapping("/test")
  public String test() {
    log.info("Message - Test()");
    return "Message - Test()";
  }

}
