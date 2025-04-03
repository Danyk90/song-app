package com.song.service.dto;

import lombok.Data;
import javax.validation.constraints.*;



@Data
public class SongsDTO {
    @NotNull
    @Positive
    private Long id;

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 1, max = 100)
    private String artist;

    @NotBlank
    @Size(min = 1, max = 100)
    private String album;

    @NotBlank
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Duration must be in the format mm:ss")
    private String duration;

    @NotBlank
    @Pattern(regexp = "^(19\\d{2}|20\\d{2}|2099)$", message = "Year must be in the format YYYY between 1900-2099")
    private String year;
}