package com.mogumogu.spring;

import com.mogumogu.spring.constant.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminEntity implements PersonEntity {

    @Id
    @Column(name = "admin_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username; //관리자 아이디

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;
}
