package com.project.resourceservice.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@JsonSerialize
@Data
public class Mp3FileResponseDto {

    private Long id;

}
