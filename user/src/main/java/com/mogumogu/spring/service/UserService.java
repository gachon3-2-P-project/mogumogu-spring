package com.mogumogu.spring.service;

import com.mogumogu.spring.ArticleEntity;
import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.dto.ArticleDto;
import com.mogumogu.spring.exception.BusinessLogicException;
import com.mogumogu.spring.exception.ExceptionCode;
import com.mogumogu.spring.mapper.ArticleMapper;
import com.mogumogu.spring.mapper.UserMapper;
import com.mogumogu.spring.repository.ArticleRepository;
import com.mogumogu.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.mogumogu.spring.dto.UserDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ArticleMapper articleMapper;
    private final ArticleRepository articleRepository;

    /**
     * 회원 id로 회원 조회
     */

    public UserDto.UserResponseDto getUser(Long userId) {
        // Entity 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // DTO로 변환 후 return
        return userMapper.toResponseDto(userEntity);
    }

    /**
     * 전체 회원 조회
     */
    @Transactional
    public List<UserDto.UserResponseDto> getAllUser() {

        List<UserEntity> userEntities = userRepository.getAllUser();
        List<UserDto.UserResponseDto> userResponseDtos = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            UserDto.UserResponseDto userResponseDto = userMapper.toResponseDto(userEntity);
            userResponseDtos.add(userResponseDto);
        }

        return userResponseDtos;
    }

    /**
     * 회원정보 수정 -> 닉네임 수정
     */
    @Transactional
    public UserDto.UserResponseDto updateUser(Long userId,UserDto.UserPatchDto userPatchDto) {

        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() ->new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        String newNickName = userPatchDto.getNickName();

        // 닉네임 중복성 확인.
        if (newNickName != null) {

            boolean isNickNameUnique = !userRepository.existsByNickName(newNickName);

            if (!isNickNameUnique) {
                throw new BusinessLogicException(ExceptionCode.DUPLICATE_NICKNAME);
            }

            userEntity.setNickName(newNickName);
        }
        return userMapper.toResponseDto(userEntity);
    }


    /**
     * 회원 탈퇴
     */
    @Transactional
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
        log.info("삭제된 User 아이디: {}",userId);
    }

    /**
     * 회원 id로 작성한 게시물들 조회
     */
    public List<ArticleDto.ArticleResponseDto> getUserArticles(Long userId) {
        // 회원 정보 조회
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessLogicException(ExceptionCode.USER_NOT_FOUND));

        // 해당 회원이 작성한 게시물들 조회
        List<ArticleEntity> articles = articleRepository.findByUser(userEntity);

        // DTO로 변환 후 return
        return articles.stream()
                .map(articleMapper::toResponseDto)
                .collect(Collectors.toList());
    }



}