package com.mogumogu.spring.controller;

import com.mogumogu.spring.dto.MessageDto;
import com.mogumogu.spring.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    /**
     * 쪽지 등록
     */
    @PostMapping("/create")
    public ResponseEntity<MessageDto.MessageResponseDto> createMessage(@RequestParam("userId") Long userId, @RequestBody MessageDto.MessageRequestDto messageRequestDto) {
        MessageDto.MessageResponseDto responseDto = messageService.createMessage(userId, messageRequestDto);
        return ResponseEntity.ok().body(responseDto);
    }


    /**
     * 쪽지 고유 id로 쪽지 조회
     */
    @GetMapping("/get")
    public ResponseEntity<?> getMessage(@RequestParam("messageId") Long messageId) {
        MessageDto.MessageResponseDto Message = messageService.getMessage(messageId);
        return ResponseEntity.ok().body(Message);
    }

    @GetMapping("/test")
    public String test() {
        log.info("Message - Test()");
        return "Message - Test()";
    }


}
