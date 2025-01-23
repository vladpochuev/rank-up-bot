package com.luxusxc.rank_up.telegram.mapper;

import com.luxusxc.rank_up.telegram.model.ChatUserId;
import com.luxusxc.rank_up.telegram.model.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.telegram.telegrambots.meta.api.objects.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {ChatUserId.class, Timestamp.class, LocalDateTime.class})
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "chatUserId", expression = "java(new ChatUserId(chatId, user.getId()))")
    @Mapping(target = "registeredAt", expression = "java(Timestamp.valueOf(LocalDateTime.now()))")
    @Mapping(target = "rankLevel", constant = "1")
    @Mapping(target = "experience", constant = "1L")
    UserEntity toUserEntity(User user, long chatId);
}
