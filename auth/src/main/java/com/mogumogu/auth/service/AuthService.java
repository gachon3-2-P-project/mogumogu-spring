//package com.mogumogu.auth.service;
//
//import com.mogumogu.auth.repository.EmailRepository;
//import com.mogumogu.common.constant.Role;
//import com.mogumogu.common.exception.BusinessLogicException;
//import com.mogumogu.common.exception.ExceptionCode;
//import com.mogumogu.config.MailService;
//import com.mogumogu.domain.EmailAuth;
//import com.mogumogu.domain.UserEntity;
//import com.mogumogu.domain.dto.UserDto;
//import com.mogumogu.domain.mapper.UserMapper;
//import com.mogumogu.user.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.util.Optional;
//import java.util.Random;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//@Transactional(readOnly = true)
//public class AuthService {
//    private final UserRepository userRepository;
//    private final UserMapper userMapper;
//    private final MailService mailService;
//    private final EmailRepository emailRepository;
//
//    @Value("${spring.mail.auth-code-expiration-millis}")
//    private long authCodeExpirationMillis;
//
//
//    @Transactional
//    public void sendCodeToEmail(String toEmail) {
//        this.checkDuplicatedEmail(toEmail);
//        String title = "MoguMogu 이메일 인증 번호";
//        String authCode = this.createCode();
//        mailService.sendEmail(toEmail, title, authCode);
//
//        //이메일 인증번호 보낸 후 DB에 저장
//        EmailAuth emailAuth = EmailAuth.builder()
//                .email(toEmail)
//                .authCode(authCode)
//                .build();
//        emailRepository.save(emailAuth);
//
//
//    }
//
//    /**
//     * 이미 회원가입한 회원인지 확인하는 메서드
//     */
//    private void checkDuplicatedEmail(String username) {
//        Optional<UserEntity> user = Optional.ofNullable(userRepository.findByUsername(username)); //username == email
//        if (user.isPresent()) {
//            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", username);
//            throw new BusinessLogicException(ExceptionCode.USER_NOT_FOUND);
//        }
//    }
//
//    @Transactional
//    public String createCode() {
//        int lenth = 6; //6자리 랜덤 코드
//        try {
//            Random random = SecureRandom.getInstanceStrong();
//            StringBuilder builder = new StringBuilder();
//            for (int i = 0; i < lenth; i++) {
//                builder.append(random.nextInt(10));
//            }
//            return builder.toString();
//        } catch (NoSuchAlgorithmException e) {
//            log.debug("MemberService.createCode() exception occur");
//            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
//        }
//    }
//
//    public boolean verifiedCode(String email, String authCode) {
//        this.checkDuplicatedEmail(email);
//
//        // 테이블에서 EmailAuth 조회
//        EmailAuth emailAuth = emailRepository.findByEmail(email);
//
//        if (emailAuth == null) {
//            throw new BusinessLogicException(ExceptionCode.EMAIL_AUTH_NOT_FOUND);
//        }
//
//        // 테이블에 저장된 코드와 입력된 코드 비교
//        return emailAuth.getAuthCode().equals(authCode);
//    }
//
//
//
//
//
//    /**
//     * 회원 생성
//     */
//    @Transactional
//    public UserDto.UserResponseDto createUser(UserDto.UserRequestDto userRequestDto) {
//
//
//        //encoding
//        if (userRequestDto.getPassword() != null)
//            userRequestDto.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
//        // RequestDto -> Entity
//        UserEntity userEntity = userMapper.toRequestEntity(userRequestDto);
//        userEntity.setRole(Role.USER);
//
//        // DB에 Entity 저장
//        UserEntity savedUser = userRepository.save(userEntity);
//
//        // Entity -> ResponseDto
//
//        return userMapper.toResponseDto(savedUser);
//    }
//}
