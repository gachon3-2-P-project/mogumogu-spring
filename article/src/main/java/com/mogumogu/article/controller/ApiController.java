package com.mogumogu.article.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiController {

  @GetMapping("/test")
  public String test() {
    log.info("Article - Test()");
    return "Article - Test()";
  }

}
