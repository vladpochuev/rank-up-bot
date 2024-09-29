package com.luxusxc.rank_up.repository;

import com.luxusxc.rank_up.model.RankEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

public interface RankRepository extends CrudRepository<RankEntity, Integer> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE ranks; ALTER SEQUENCE ranks_level_seq RESTART WITH 1", nativeQuery = true)
    void truncate();
}