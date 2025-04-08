package com.project.mp3resourceservice.dto;

import java.util.List;

public class Mp3IdListResponseDto {

    private List<Long> ids;

    public Mp3IdListResponseDto(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

}
