package com.project.resourceservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.Transient;
import lombok.Data;

@JsonSerialize
@Data
public class Mp3MetadataDto {

    private Long id;

    private String title;
    private String artist;
    private String album;
    private String name;
    private String duration;
    private String year;
    @Transient
    private byte[] data;

}
