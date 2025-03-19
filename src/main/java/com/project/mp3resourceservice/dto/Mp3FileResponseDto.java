package com.project.mp3resourceservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
public class Mp3FileResponseDto {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
