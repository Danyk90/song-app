package com.song.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Mp3IdListResponseDto {

    private List<Long> ids;

}
