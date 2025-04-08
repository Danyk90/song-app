package com.song.service.util;
// src/main/java/com/song/service/mapper/ResourcesMapper.java

import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;

public class MetaDataDtoToEntityMapper {

    public static SongsMetaDataEntity toEntity(SongsDTO dto) {
        if (dto == null) {
            return null;
        }
        SongsMetaDataEntity entity = new SongsMetaDataEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setArtist(dto.getArtist());
        entity.setAlbum(dto.getAlbum());
        entity.setDuration(dto.getDuration());
        entity.setYear(String.valueOf(dto.getYear()));
        return entity;
    }

    public static SongsDTO toDto(SongsMetaDataEntity entity) {
        if (entity == null) {
            return null;
        }
        SongsDTO dto = new SongsDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setArtist(entity.getArtist());
        dto.setAlbum(entity.getAlbum());
        dto.setDuration(entity.getDuration());
        dto.setYear(Integer.valueOf(entity.getYear()));
        return dto;
    }
}