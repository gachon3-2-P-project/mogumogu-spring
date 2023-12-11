package com.mogumogu.spring.repository;
import com.mogumogu.spring.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailAuth, Long> {

    EmailAuth findByEmail(String email);

    void deleteByEmail(String email);

}
