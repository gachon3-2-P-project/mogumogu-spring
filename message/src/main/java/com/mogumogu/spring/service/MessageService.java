package com.mogumogu.spring.service;

import com.mogumogu.spring.dto.ArticleDto;
import com.mogumogu.spring.exception.BusinessLogicException;
import com.mogumogu.spring.exception.ExceptionCode;
import com.mogumogu.spring.ArticleEntity;
import com.mogumogu.spring.MessageEntity;
import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.dto.MessageDto;
import com.mogumogu.spring.mapper.ArticleMapper;
import com.mogumogu.spring.mapper.MessageMapper;
import com.mogumogu.spring.repository.ArticleRepository;
import com.mogumogu.spring.repository.MessageRepository;
import com.mogumogu.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MessageService {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private  final MessageRepository messageRepository;
    private final MessageMapper messageMapper;
    private final ArticleMapper articleMapper;

    /**
     * 쪽지 생성
     */
    @Transactional
    public MessageDto.MessageResponseDto createMessage(Long userId, MessageDto.MessageRequestDto messageRequestDto) {
        log.info("------");
        log.info(String.valueOf(userId));
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
        log.info(String.valueOf(userId));

        ArticleEntity articleEntity = articleRepository.findById(messageRequestDto.getArticleId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        // receiver의 닉네임이 DB에 등록된 사용자인지 확인
        if (!userRepository.existsByNickName(messageRequestDto.getReceiver())) {
            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
        }

        MessageEntity savedMessage = messageRepository.save(messageMapper.toRequestEntity(messageRequestDto, userEntity, articleEntity));
        savedMessage.setSender(userEntity.getNickName());
        MessageDto.MessageResponseDto responseDto = messageMapper.toResponseDto(savedMessage);
        responseDto.setUserId(userId);
        responseDto.setArticleId(messageRequestDto.getArticleId());

        return responseDto;
    }

    /**
     * 쪽지 고유 아이디로 쪽지 조회
     */
    public MessageDto.MessageResponseDto getMessage(Long messageId) {

        return messageMapper.toResponseDto(messageRepository.findById(messageId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.Message_IS_NOT_EXIST)));

    }

    @Transactional
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
        log.info("삭제된 Message: {}",messageId);
    }

    /**
     * 해당 게시물 쪽지 조회
     */

    public List<ArticleDto.ArticleResponseDto> getArticleMessages(Long articleId) {

        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        // 게시물과 연관된 메시지들
        List<MessageEntity> messages = articleEntity.getMessages();
        List<MessageDto.MessageResponseDto> messageDtos = new ArrayList<>();

        // 메시지들을 DTO로 변환하여 리스트에 추가
        for (MessageEntity messageEntity : messages) {
            MessageDto.MessageResponseDto messageResponseDto = messageMapper.toResponseDto(messageEntity);

            messageResponseDto.setUserId(messageEntity.getUser().getId());
            //messageResponseDto.setNickName(messageEntity.getUser().getNickName());

            messageDtos.add(messageResponseDto);
        }

        List<ArticleDto.ArticleResponseDto> result = new ArrayList<>();
        ArticleDto.ArticleResponseDto articleResponseDto = articleMapper.toResponseDto(articleEntity);
        articleResponseDto.setMessages(messageDtos);
        result.add(articleResponseDto);

        return result;
    }

    /**
     * 쪽지함 구현
     */
    public List<ArticleDto.ArticleResponseDto> getMessageStorage(Long userId) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 수신자로부터 온 메시지 조회
        List<MessageEntity> receivedMessages = messageRepository.findByReceiver(userEntity.getNickName());

        // 발신자로부터 온 메시지 조회
        List<MessageEntity> sentMessages = messageRepository.findBySender(userEntity.getNickName());

        Set<Long> processedArticleIds = new HashSet<>(); // 중복 체크를 위한 Set

        List<ArticleDto.ArticleResponseDto> articleResponses = new ArrayList<>();

        // 수신자로부터 온 메시지 처리
        for (MessageEntity messageEntity : receivedMessages) {
            // 중복된 게시물이 아직 처리되지 않았으면 처리하고, Set에 추가
            if (processedArticleIds.add(messageEntity.getArticle().getId())) {
                ArticleEntity articleEntity = articleRepository.findById(messageEntity.getArticle().getId())
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));
                ArticleDto.ArticleResponseDto articleResponse = articleMapper.toResponseDto(articleEntity);
                articleResponses.add(articleResponse);
            }
        }

        // 발신자로부터 온 메시지 처리
        for (MessageEntity messageEntity : sentMessages) {
            // 중복된 게시물이 아직 처리되지 않았으면 처리하고, Set에 추가
            if (processedArticleIds.add(messageEntity.getArticle().getId())) {
                ArticleEntity articleEntity = articleRepository.findById(messageEntity.getArticle().getId())
                        .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));
                ArticleDto.ArticleResponseDto articleResponse = articleMapper.toResponseDto(articleEntity);
                articleResponses.add(articleResponse);
            }
        }

        return articleResponses;
    }



}
