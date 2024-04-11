package com.edu.basaoc.model.mapper;

import com.edu.basaoc.model.ChatMessageRequestDto;
import com.edu.basaoc.model.entity.ChatMessage;
import com.edu.basaoc.model.entity.Profile;
import com.edu.basaoc.service.ProfileService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.mapstruct.*;


public interface ChatMessageMapper {

    ChatMessage toEntity(ChatMessageRequestDto chatMessageRequestDto);

    ChatMessageRequestDto toDto(ChatMessage chatMessage);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ChatMessage partialUpdate(ChatMessageRequestDto chatMessageRequestDto, @MappingTarget ChatMessage chatMessage);


}