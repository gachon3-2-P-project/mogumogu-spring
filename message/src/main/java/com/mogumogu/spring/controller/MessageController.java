package com.mogumogu.spring.controller;

import com.mogumogu.spring.dto.ArticleDto;
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
     * 게시글Id로 작성된 쪽지 조회
     */

    @GetMapping("/getArticleMessages")
    public ResponseEntity<List<ArticleDto.ArticleResponseDto>> getArticleMessages(@RequestParam Long articleId) {
        List<ArticleDto.ArticleResponseDto> articlesWithMessages = messageService.getArticleMessages(articleId);
        return ResponseEntity.ok().body(articlesWithMessages);
    }

    /**
     * 쪽지함 쪽지 조회
     */
    @GetMapping("/getMessageStorage")
    public ResponseEntity<List<ArticleDto.ArticleResponseDto>> getMessageStorage(@RequestParam Long userId) {

        List<ArticleDto.ArticleResponseDto> articleResponses = messageService.getMessageStorage(userId);
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



}
