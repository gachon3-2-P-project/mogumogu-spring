package com.mogumogu.spring.service;

import com.mogumogu.spring.ArticleEntity;
import com.mogumogu.spring.MessageEntity;
import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.constant.Transaction;
import com.mogumogu.spring.dto.MessageDto;
import com.mogumogu.spring.exception.BusinessLogicException;
import com.mogumogu.spring.exception.ExceptionCode;
import com.mogumogu.spring.mapper.ArticleMapper;
import com.mogumogu.spring.mapper.MessageMapper;
import com.mogumogu.spring.repository.ArticleRepository;
import com.mogumogu.spring.repository.MessageRepository;
import com.mogumogu.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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



    @Transactional
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
        log.info("삭제된 Message: {}",messageId);
    }


    public List<MessageDto.MessageResponseDto> getMessageStorage(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 해당 사용자가 보낸 메시지 조회
        List<MessageEntity> sentMessages = messageRepository.findBySender(userEntity.getNickName());

        // 해당 사용자가 받은 메시지 조회
        List<MessageEntity> receivedMessages = messageRepository.findByReceiver(userEntity.getNickName());

        // 받은 메시지와 보낸 메시지를 합침
        List<MessageEntity> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        return allMessages.stream()
                .map(messageEntity -> {
                    MessageDto.MessageResponseDto messageResponseDto = messageMapper.toResponseDto(messageEntity);

                    // 해당 메시지가 속한 게시물 정보 추가
                    ArticleEntity articleEntity = messageEntity.getArticle();
                    if (articleEntity != null) {
                        messageResponseDto.setArticleId(articleEntity.getId());
                        messageResponseDto.setArticleTitle(articleEntity.getTitle());
                    }

                    // receiverId와 senderId 설정
                    String receiverNickName = messageEntity.getReceiver();
                    Long receiverId = userRepository.findIdByNickName(receiverNickName);
                    messageResponseDto.setReceiverId(receiverId);

                    String senderNickName = messageEntity.getSender();
                    Long senderId = userRepository.findIdByNickName(senderNickName);
                    messageResponseDto.setSenderId(senderId);

                    return messageResponseDto;
                })
                .collect(Collectors.toList());
    }




    public List<MessageDto.MessageArticleResponseDto> getArticleMessages(Long articleId, Long userId) {
        ArticleEntity articleEntity = articleRepository.findById(articleId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 게시물 작성자의 닉네임과 사용자 닉네임 가져오기
        String authorNickName = articleEntity.getUser().getNickName();
        String userNickName = userEntity.getNickName();

        // 게시물 작성자가 쪽지 보내는 사용자 메시지 가져오기
        List<MessageEntity> authorMessages = articleEntity.getMessages().stream()
                .filter(messageEntity ->
                        authorNickName.equals(messageEntity.getSender()) || userNickName.equals(messageEntity.getReceiver()))
                .collect(Collectors.toList());

        // 사용자가 보낸 메시지 가져오기
        List<MessageEntity> userMessages = articleEntity.getMessages().stream()
                .filter(messageEntity -> userNickName.equals(messageEntity.getSender()) || userNickName.equals(messageEntity.getReceiver()))
                .collect(Collectors.toList());

        // 두 리스트 합치기
        Set<MessageEntity> allMessages = new HashSet<>();
        allMessages.addAll(authorMessages);
        allMessages.addAll(userMessages);


        List<MessageDto.MessageArticleResponseDto> messageDtos = allMessages.stream()
                .map(messageEntity -> {
                    MessageDto.MessageArticleResponseDto messageResponseDto = messageMapper.toArticleResponseDto(messageEntity);

                    // receiverId와 senderId 설정
                    String receiverNickName = messageEntity.getReceiver();
                    Long receiverId = userRepository.findIdByNickName(receiverNickName);
                    messageResponseDto.setReceiverId(receiverId);

                    String senderNickName = messageEntity.getSender();
                    Long senderId = userRepository.findIdByNickName(senderNickName);
                    messageResponseDto.setSenderId(senderId);

                    Transaction transactionStatus = articleEntity.getTransactionStatus();
                    messageResponseDto.setTransactionStatus(transactionStatus);

                    return messageResponseDto;
                })
                .sorted(Comparator.comparingLong(MessageDto.MessageArticleResponseDto::getId))
                .collect(Collectors.toList());

        return messageDtos;
    }



}
