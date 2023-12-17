package com.mogumogu.spring.dto;

import java.time.LocalTime;
import lombok.*;

public class MessageDto {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageRequestDto {

        private Long articleId;

        private String content; //내용

        private String receiver; // 수신자 닉네임


    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MessageResponseDto {

        private Long id;

        private Long userId; //게시물 작성한 사용자 id


        private Long articleId; //해당 거래 게시물 id

        private String receiver; // 수신자 닉네임

        private String articleTitle;

        private String content; //내용

        private LocalTime createdAt;

        private String sender;

        private Long receiverId;
        private Long senderId;


    }
}
