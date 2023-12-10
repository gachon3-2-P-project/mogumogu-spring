package com.mogumogu.spring.auth;

import com.mogumogu.spring.AdminEntity;
import com.mogumogu.spring.UserEntity;
import com.mogumogu.spring.repository.AdminRepository;
import com.mogumogu.spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AdminRepository adminRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : 진입");
        System.out.println(username);

        if (username.contains("admin_")) {
            AdminEntity adminEntity = adminRepository.findByUsername(username);

            return new PrincipalDetails(adminEntity);
        } else {
            UserEntity userEntity = userRepository.findByUsername(username);



            return new PrincipalDetails(userEntity);
        }
    }
}
