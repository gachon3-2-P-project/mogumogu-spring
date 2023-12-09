package com.mogumogu.spring.service;

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
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        ArticleEntity articleEntity = articleRepository.findById(messageRequestDto.getArticleId())
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        MessageEntity savedMessage = messageRepository.save(messageMapper.toRequestEntity(messageRequestDto, userEntity, articleEntity));
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



}
