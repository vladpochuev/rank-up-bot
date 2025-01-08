package com.luxusxc.rank_up.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CallbackQueryMapper {
    CallbackQueryMapper INSTANCE = Mappers.getMapper(CallbackQueryMapper.class);

    Message toMessage(CallbackQuery query);
}
