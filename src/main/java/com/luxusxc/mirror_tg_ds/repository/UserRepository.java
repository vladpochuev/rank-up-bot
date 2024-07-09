package com.luxusxc.mirror_tg_ds.repository;

import com.luxusxc.mirror_tg_ds.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
}

