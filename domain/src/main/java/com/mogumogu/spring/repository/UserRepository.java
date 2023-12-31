package com.mogumogu.spring.repository;

import com.mogumogu.spring.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT m FROM UserEntity m")
    List<UserEntity> getAllUser();

    boolean existsByNickName(String nickName);

    UserEntity findByUsername(String username);

    @Query("SELECT u.id FROM UserEntity u WHERE u.nickName = :nickName")
    Long findIdByNickName(String nickName);



}
