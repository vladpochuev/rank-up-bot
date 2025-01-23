package com.luxusxc.rank_up.web.mapper;

import com.luxusxc.rank_up.common.model.Config;
import com.luxusxc.rank_up.web.model.WebConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ConfigMapper {
    ConfigMapper INSTANCE = Mappers.getMapper(ConfigMapper.class);

    @Mapping(source = "levelUpMessageFormat", target = "levelUpMessage")
    WebConfig toWebRankUpConfig(Config config);
}
