package com.edu.basaoc.model.mapper;

import com.edu.basaoc.model.ArtistDto;
import com.edu.basaoc.model.GenreDto;
import com.edu.basaoc.model.entity.Artist;
import com.edu.basaoc.model.entity.Genre;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper
public interface GenreDtoMapper {
    Genre dtoToEntity(GenreDto dto);

    GenreDto entityToDto(Genre entity);

    Genre updateEntityFromDto(GenreDto dto, @MappingTarget Genre genre);
}
