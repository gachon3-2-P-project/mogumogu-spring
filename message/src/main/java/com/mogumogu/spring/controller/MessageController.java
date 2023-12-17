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



//    /**
//     * 게시글Id로 작성된 쪽지 조회
//     */
//
//    @GetMapping("/getArticleMessages")
//    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleMessages(@RequestParam Long articleId) {
//        List<MessageDto.MessageResponseDto> articlesWithMessages = messageService.getArticleMessages(articleId);
//        return ResponseEntity.ok().body(articlesWithMessages);
//    }

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

//    /**
//     * 게시물 작성자가 해당 게시물에 작성한 메시지 조회
//     */
//    @GetMapping("/getArticleAuthorMessages")
//    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleAuthorMessages(@RequestParam Long articleId, Long receiverUserId) {
//
//        List<MessageDto.MessageResponseDto> messageResponses = messageService.getArticleAuthorMessages(articleId, receiverUserId);
//        return ResponseEntity.ok(messageResponses);
//
//    }

//    /**
//     * 사용자가 해당 게시물에 작성한 메시지 조회
//     */
//    @GetMapping("/getArticleSenderMessages")
//    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleSenderMessages(@RequestParam Long articleId, Long userId) {
//
//        List<MessageDto.MessageResponseDto> messageResponses = messageService.getArticleSenderMessages(articleId, userId);
//        return ResponseEntity.ok(messageResponses);
//
//    }

    /**
     * 게시물 작성자와 1:1 대화 내용
     */

    @GetMapping("/getArticleMessages")
    public ResponseEntity<List<MessageDto.MessageResponseDto>> getArticleMessages(@RequestParam Long articleId, Long userId) {
        List<MessageDto.MessageResponseDto> articlesWithMessages = messageService.getArticleMessages(articleId, userId);
        return ResponseEntity.ok().body(articlesWithMessages);
    }





}
