package com.luxusxc.rank_up.telegram.repository;

import com.luxusxc.rank_up.telegram.model.ChatUserId;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, ChatUserId> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    Iterable<UserEntity> findAll(@Param("userId") long userId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM users WHERE chat_id = :chatId", nativeQuery = true)
    Iterable<UserEntity> findALlByChatId(@Param("chatId") long chatId);

    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM users ORDER BY rank_level, experience DESC LIMIT 1", nativeQuery = true)
    Optional<UserEntity> findHighestLevelUser();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE chat_id = :chatId", nativeQuery = true)
    void deleteAllChatUsers(@Param("chatId") long chatId);
}
