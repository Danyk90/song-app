package com.project.resourceservice.client;

import com.project.resourceservice.dto.Mp3MetadataDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "song-service")
public interface SongServiceClient {

    @PostMapping("/songs")
    void saveResource(Mp3MetadataDto mp3MetadataDto);

    @DeleteMapping("/songs")
    void deleteResource(@RequestParam("id") String ids);

    @GetMapping("/songs")
    List<Mp3MetadataDto> getResourcesByIds(@RequestParam List<Long> ids);
}
