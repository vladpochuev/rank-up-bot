package com.luxusxc.rank_up.mapper;

import com.luxusxc.rank_up.model.RankUpConfig;
import com.luxusxc.rank_up.model.WebRankUpConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RankUpConfigMapper {
    RankUpConfigMapper INSTANCE = Mappers.getMapper(RankUpConfigMapper.class);
    @Mapping(source = "levelUpMessageFormat", target = "levelUpMessage")
    WebRankUpConfig toWebRankUpConfig(RankUpConfig config);
}
