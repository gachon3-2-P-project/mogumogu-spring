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

    public List<MessageDto.MessageResponseDto> getMessageStorage(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 해당 사용자가 보낸 메시지 조회
        List<MessageEntity> sentMessages = messageRepository.findBySender(userEntity.getNickName());

        // 해당 사용자가 받은 메시지 조회
        List<MessageEntity> receivedMessages = messageRepository.findByReceiver(userEntity.getNickName());

        // 받은 메시지와 보낸 메시지를 합친 후 중복을 제거
        List<MessageEntity> allMessages = new ArrayList<>();
        allMessages.addAll(sentMessages);
        allMessages.addAll(receivedMessages);

        // 확인된 receiver와 sender를 저장하기 위한 Set
        Set<String> checkedReceivers = new HashSet<>();
        Set<String> checkedSenders = new HashSet<>();

        return allMessages.stream()
                .filter(messageEntity -> {

                    String receiver = messageEntity.getReceiver();
                    String sender = messageEntity.getSender();

                    if (receiver != null && !checkedReceivers.contains(receiver)) {
                        if (receiver.equals(userEntity.getNickName())) {
                            checkedReceivers.add(receiver);
                            return true;
                        }
                    }

                    if (sender != null && !checkedSenders.contains(sender)) {
                        // userId와 senderId가 같은 경우에도 추가
                        if (sender.equals(userEntity.getNickName())) {
                            checkedSenders.add(sender);
                            return true;
                        }
                    }

                    return false;
                })
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







    public List<MessageDto.MessageResponseDto> getArticleMessages(Long articleId, Long userId) {
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


        List<MessageDto.MessageResponseDto> messageDtos = allMessages.stream()
                .map(messageEntity -> {
                    MessageDto.MessageResponseDto messageResponseDto = messageMapper.toResponseDto(messageEntity);

                    // receiverId와 senderId 설정
                    String receiverNickName = messageEntity.getReceiver();
                    Long receiverId = userRepository.findIdByNickName(receiverNickName);
                    messageResponseDto.setReceiverId(receiverId);

                    String senderNickName = messageEntity.getSender();
                    Long senderId = userRepository.findIdByNickName(senderNickName);
                    messageResponseDto.setSenderId(senderId);

                    return messageResponseDto;
                })
                .sorted(Comparator.comparingLong(MessageDto.MessageResponseDto::getId))
                .collect(Collectors.toList());

        return messageDtos;
    }




//    /**
//     * 게시물 작성자가 해당 게시물에 작성한 메시지 조회
//     */
//
//    public List<MessageDto.MessageResponseDto> getArticleAuthorMessages(Long articleId, Long userId) {
//
//        ArticleEntity articleEntity = articleRepository.findById(articleId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));
//
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
//
//
//        //게시물 작성자의 닉네임 가져오기
//        String authorNickName = articleEntity.getUser().getNickName();
//        log.info(authorNickName);
//
//        //게시물 작성자가 쪽지 보내는 사용자 닉네임 가져오기
//        String userNickName = userEntity.getNickName();
//        log.info(userNickName);
//
//        //게시물 작성자가 보낸 메시지만 가져오기
//        List<MessageEntity> messages = articleEntity.getMessages().stream()
//                .filter(messageEntity -> authorNickName.equals(messageEntity.getSender()) && userNickName.equals(messageEntity.getReceiver()))
//                .collect(Collectors.toList());
//
//        log.info("Number of Messages for Author: {}", messages.size());
//
//
//        List<MessageDto.MessageResponseDto> messageDtos = messages.stream()
//                .map(messageEntity -> messageMapper.toResponseDto(messageEntity))
//                .collect(Collectors.toList());
//
//        return messageDtos;
//
//
//    }

//    /**
//     * 사용자가 해당 게시물에 작성한 메시지 조회
//     */
//
//    public List<MessageDto.MessageResponseDto> getArticleSenderMessages(Long articleId, Long userId) {
//
//        ArticleEntity articleEntity = articleRepository.findById(articleId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.ARTICLE_NOT_EXIST));
//
//        UserEntity userEntity = userRepository.findById(userId)
//                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));
//
//        //사용자 닉네임 가져오기
//        String userNickName = userEntity.getNickName();
//        log.info(userNickName);
//
//        //게시물 작성자가 보낸 메시지만 가져오기
//        List<MessageEntity> messages = articleEntity.getMessages().stream()
//                .filter(messageEntity -> userNickName.equals(messageEntity.getSender()))
//                .collect(Collectors.toList());
//
//        log.info("Number of Messages for Author: {}", messages.size());
//
//
//        List<MessageDto.MessageResponseDto> messageDtos = messages.stream()
//                .map(messageEntity -> messageMapper.toResponseDto(messageEntity))
//                .collect(Collectors.toList());
//
//        return messageDtos;
//
//
//    }



}
