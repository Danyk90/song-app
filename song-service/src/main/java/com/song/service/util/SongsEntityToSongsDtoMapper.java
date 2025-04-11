package com.song.service.util;

import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;

public class SongsEntityToSongsDtoMapper {

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
        dto.setYear(entity.getYear());
        return dto;
    }
}
