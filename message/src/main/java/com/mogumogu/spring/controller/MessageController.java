package com.mogumogu.spring.controller;

import com.mogumogu.spring.dto.MessageDto;
import com.mogumogu.spring.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 해당 게시물 쪽지 조회
     */
    @GetMapping("/getMessageStorage")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getMessageStorage(@RequestParam Long userId) {

        List<MessageDto.MessageResponseDto> articleResponses = messageService.getMessageStorage(userId);
        return ResponseEntity.ok(articleResponses);

    }

    /**
     * 쪽지 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteMessage(@RequestParam Long messageId) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.ok().body("Deleted Message Id : " + messageId);
    }

    /**
     * 게시물 작성자와 1:1 대화 내용
     */

    @GetMapping("/getArticleMessages")
    public ResponseEntity<List<MessageDto.MessageArticleResponseDto>> getArticleMessages(@RequestParam Long articleId, Long userId) {
        List<MessageDto.MessageArticleResponseDto> articlesWithMessages = messageService.getArticleMessages(articleId, userId);

        return ResponseEntity.ok().body(articlesWithMessages);
    }



}
