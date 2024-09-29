package com.luxusxc.rank_up.repository;

import com.luxusxc.rank_up.model.ChatUserId;
import com.luxusxc.rank_up.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, ChatUserId> {
}
