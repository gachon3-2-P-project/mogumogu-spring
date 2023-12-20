package com.mogumogu.spring;

import com.mogumogu.TimeStamp;
import com.mogumogu.spring.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity extends TimeStamp implements PersonEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; //사용자 아이디 (= 사용자 이메일)

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    private String nickName;


}
