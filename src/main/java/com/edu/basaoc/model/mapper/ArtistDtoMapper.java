package com.edu.basaoc.model.mapper;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface ArtistDtoMapper {
    Artist dtoToEntity(ArtistDto dto);

    ArtistDto entityToDto(Artist entity);

    Artist updateEntityFromDto(ArtistDto dto, @MappingTarget Artist artist);

}
