package com.luxusxc.rank_up.repository;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends CrudRepository<UserEntity, ChatUserId> {
    @Modifying
    @Transactional
    @Query(value = "SELECT * FROM users WHERE user_id = :userId", nativeQuery = true)
    Iterable<UserEntity> findAll(@Param("userId") long userId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM users WHERE chat_id = :chatId", nativeQuery = true)
    void deleteAllChatUsers(@Param("chatId") long chatId);
}
