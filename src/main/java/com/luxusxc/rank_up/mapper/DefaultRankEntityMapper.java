package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.DefaultRankEntity;
import com.luxusxc.rank_up.model.RankEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DefaultRankEntityMapper {
    DefaultRankEntityMapper INSTANCE = Mappers.getMapper(DefaultRankEntityMapper.class);

    RankEntity toRankEntity(DefaultRankEntity defaultRankEntity);
}
