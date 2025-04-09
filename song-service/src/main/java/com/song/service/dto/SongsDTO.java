package com.song.service.dto;

import jakarta.validation.constraints.*;
import lombok.Data;


@Data
public class SongsDTO {
    @NotNull
    @Positive
    private Long id;


    @NotNull(message = "Song name is required")
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 100)
    private String artist;

    @NotBlank
    @Size(min = 1, max = 100)
    private String album;

    @NotBlank
    @Pattern(regexp = "^[0-5][0-9]:[0-5][0-9]$", message = "Duration must be in the format mm:ss")
    private String duration;


    @Pattern(regexp = "^(19\\d{2}|20\\d{2})$", message = "Year must be between 1900 and 2099")
    private String year;
}