package com.luxusxc.rank_up.web.mapper;

import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.web.model.WebConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface WebConfigMapper {
    WebConfigMapper INSTANCE = Mappers.getMapper(WebConfigMapper.class);

    @Mapping(source = "levelUpMessage", target = "levelUpMessageFormat")
    Config toRankUpConfig(WebConfig webConfig);
}
