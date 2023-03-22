package com.edu.basaoc.model.mapper;

import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.ProfileResponseDto;
import com.edu.basaoc.model.entity.Genre;
import com.edu.basaoc.model.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper
public interface ProfileResponseDtoMapper {
    Profile dtoToEntity(ProfileResponseDto dto);

    @Mapping(target = "profilePictureUrl", ignore = true)
    ProfileResponseDto entityToDto(Profile profile);

    Profile updateEntityFromDto(ProfileResponseDto dto, @MappingTarget Profile profile);
}
