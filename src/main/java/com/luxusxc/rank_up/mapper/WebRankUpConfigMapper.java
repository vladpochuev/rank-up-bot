package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebRankUpConfigMapper {
    WebRankUpConfigMapper INSTANCE = Mappers.getMapper(WebRankUpConfigMapper.class);

    @Mapping(source = "levelUpMessage", target = "levelUpMessageFormat")
    RankUpConfig toRankUpConfig(WebRankUpConfig webConfig);
}
