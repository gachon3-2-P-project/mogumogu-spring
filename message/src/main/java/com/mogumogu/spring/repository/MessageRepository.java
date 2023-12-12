package com.mogumogu.spring.repository;

import com.mogumogu.spring.MessageEntity;
import com.mogumogu.spring.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Long> {
    List<MessageEntity> findByUser(UserEntity userEntity);

    List<MessageEntity> findByReceiver(String receiver);

    List<MessageEntity> findBySender(String sender);


}
