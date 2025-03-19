package com.project.mp3resourceservice.mapper;

import com.project.mp3resourceservice.dto.Mp3FileResponseDto;
import com.project.mp3resourceservice.entity.Mp3File;

public class Mp3FileToResponseDtoMapper {

    public static Mp3FileResponseDto map(Mp3File mp3File) {
        Mp3FileResponseDto mp3FileResponseDto = new Mp3FileResponseDto();
        mp3FileResponseDto.setId(mp3File.getId());
        return mp3FileResponseDto;
    }

}
