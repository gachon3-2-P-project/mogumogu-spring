package com.mogumogu.spring.service;

import com.mogumogu.spring.EmailAuth;
import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.constant.Role;
import com.mogumogu.spring.dto.UserDto;
import com.mogumogu.spring.exception.BusinessLogicException;
import com.mogumogu.spring.exception.ExceptionCode;
import com.mogumogu.spring.mapper.UserMapper;
import com.mogumogu.spring.repository.EmailRepository;
import com.mogumogu.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JoinService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MailService mailService;
    private final EmailRepository emailRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;

    /**
     * 인증 코드 보낸 뒤 10 분마다 해당 데이터 삭제
     */

    @Scheduled(fixedRate = 600000)  // 10분마다 실행
    @Transactional
    public void deleteExpiredAuthCodes() {

        // 현재 시간을 기준으로 10분 이전에 만료된 인증 코드를 조회
        long currentTimeMinusOneMinute = System.currentTimeMillis() - 600000;
        log.info(String.valueOf(currentTimeMinusOneMinute));

        // 현재 시간을 기준으로 10분 이전에 만료된 인증 코드를 조회
        List<EmailAuth> expiredAuthCodes = emailRepository.findByAuthCodeExpirationMillisLessThan(currentTimeMinusOneMinute);

        // 만료된 인증 코드 삭제
        for (EmailAuth expiredAuthCode : expiredAuthCodes) {
            emailRepository.delete(expiredAuthCode);
            log.info("Expired Email : {}", expiredAuthCode.getEmail());
        }


    }


    @Transactional
    public void sendCodeToEmail(String toEmail) {
        String title = "MoguMogu 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);

        //이메일 인증번호 보낸 후 DB에 저장
        EmailAuth emailAuthEntity = EmailAuth.builder()
                .email(toEmail)
                .authCode(authCode)
                .authCodeExpirationMillis(authCodeExpirationMillis)
                .build();
        emailRepository.save(emailAuthEntity);


    }



    @Transactional
    public String createCode() {
        int lenth = 6; //6자리 랜덤 코드
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < lenth; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new BusinessLogicException(ExceptionCode.NO_SUCH_ALGORITHM);
        }
    }

    @Transactional
    public boolean verifiedCode(String email, String authCode) {
        //this.checkDuplicatedEmail(email);

        // 테이블에서 EmailAuth 조회
        EmailAuth emailAuth = emailRepository.findByEmail(email);

        if (emailAuth == null) {
            throw new BusinessLogicException(ExceptionCode.EMAIL_AUTH_NOT_FOUND);
        }

        // 테이블에 저장된 코드와 입력된 코드 비교
        return emailAuth.getAuthCode().equals(authCode);
    }


    /**
     * 회원 생성
     */
    @Transactional
    public UserDto.UserResponseDto createUser(UserDto.UserRequestDto userRequestDto) {


        //encoding
        if (userRequestDto.getPassword() != null)
            userRequestDto.setPassword(bCryptPasswordEncoder.encode(userRequestDto.getPassword()));
        // RequestDto -> Entity
        UserEntity userEntity = userMapper.toRequestEntity(userRequestDto);
        userEntity.setRole(Role.USER);

        // DB에 Entity 저장
        UserEntity savedUser = userRepository.save(userEntity);

        // Entity -> ResponseDto

        return userMapper.toResponseDto(savedUser);
    }
}
