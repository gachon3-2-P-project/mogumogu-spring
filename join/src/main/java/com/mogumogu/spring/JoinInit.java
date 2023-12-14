package com.mogumogu.spring;

import com.mogumogu.spring.constant.Role;
import com.mogumogu.spring.repository.AdminRepository;
import com.mogumogu.spring.repository.ArticleRepository;
import com.mogumogu.spring.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class JoinInit {
//
//    private final UserRepository userRepository;
//    private final AdminRepository adminRepository;
//    private final ArticleRepository articleRepository;
//    private final BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @PostConstruct
//    private void initFirst(){
//        initAdmins();
//        initUsers();
//        initArticles();
//    }
//
//    @Transactional
//    public void initAdmins() {
//        AdminEntity admin = new AdminEntity();
//        admin.setUsername("admin_0"); //관리자아이디
//        admin.setPassword(bCryptPasswordEncoder.encode("admin_" + 0));
//        admin.setRole(Role.ADMIN);
//        adminRepository.save(admin);
//    }
//
//
//
//
//}
