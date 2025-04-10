package com.project.resourceservice.mapper;

import com.project.resourceservice.dto.Mp3FileResponseDto;
import com.project.resourceservice.entity.Mp3File;

public class Mp3FileToResponseDtoMapper {

    public static Mp3FileResponseDto mapToResponseDto(Mp3File mp3File) {
        Mp3FileResponseDto mp3FileResponseDto = new Mp3FileResponseDto();
        mp3FileResponseDto.setId(mp3File.getId());
        return mp3FileResponseDto;
    }

}
