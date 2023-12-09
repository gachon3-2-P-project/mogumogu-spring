package com.mogumogu.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class TransactionController {

  @GetMapping("/test")
  public String test() {
    log.info("Transaction - Test()");
    return "Transaction - Test()";
  }

}
