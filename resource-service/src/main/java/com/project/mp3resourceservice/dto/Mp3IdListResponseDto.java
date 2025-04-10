package com.project.mp3resourceservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class Mp3IdListResponseDto {

    private List<Long> ids;

    public Mp3IdListResponseDto(List<Long> ids) {
        this.ids = ids;
    }

}
