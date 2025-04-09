package com.project.mp3resourceservice.mapper;

import com.project.mp3resourceservice.dto.Mp3MetadataDto;
import com.project.mp3resourceservice.entity.Mp3File;

public class Mp3FileMapper {

    public static Mp3MetadataDto toDto(Mp3File mp3File) {
        Mp3MetadataDto dto = new Mp3MetadataDto();
        dto.setId(mp3File.getId());
        return dto;
    }
}
