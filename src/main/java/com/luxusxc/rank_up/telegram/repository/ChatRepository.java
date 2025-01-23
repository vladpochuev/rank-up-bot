package com.luxusxc.rank_up.telegram.repository;

import com.luxusxc.rank_up.telegram.model.ChatEntity;
import org.springframework.data.repository.CrudRepository;

public interface ChatRepository extends CrudRepository<ChatEntity, Long> {
}
