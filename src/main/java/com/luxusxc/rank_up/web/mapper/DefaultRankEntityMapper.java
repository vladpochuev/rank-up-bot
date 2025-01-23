package com.luxusxc.rank_up.web.mapper;

import com.luxusxc.rank_up.web.model.DefaultRankEntity;
import com.luxusxc.rank_up.common.model.RankEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface DefaultRankEntityMapper {
    DefaultRankEntityMapper INSTANCE = Mappers.getMapper(DefaultRankEntityMapper.class);

    RankEntity toRankEntity(DefaultRankEntity defaultRankEntity);
}
