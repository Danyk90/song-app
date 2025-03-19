package com.song.service.dto;

import lombok.Data;

@Data
public class SongsDTO {
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    private String year;
}