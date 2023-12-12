package com.mogumogu.spring.repository;
import com.mogumogu.spring.EmailAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailRepository extends JpaRepository<EmailAuth, Long> {

    EmailAuth findByEmail(String email);

    @Modifying
    @Query("DELETE FROM EmailAuth e WHERE e.email = :email")
    void deleteByEmail(@Param("email") String email);

    List<EmailAuth> findByAuthCodeExpirationMillisLessThan(long currentTimeMillis);

}
