package com.song.service.controller;

import com.song.service.dto.Mp3IdListResponseDto;
import com.song.service.dto.SongsDTO;
import com.song.service.metadataservice.SongsMetaDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/songs")
public class SongServiceController {

    private final SongsMetaDataService songsMetaDataService;

    public SongServiceController(SongsMetaDataService songsMetaDataService) {
        this.songsMetaDataService = songsMetaDataService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongsDTO> getResourceById(@PathVariable Long id) {
        log.info("Fetching resource with ID: {}", id);

        Optional<SongsDTO> resource = Optional.of(songsMetaDataService.findById(id));
        return resource.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SongsDTO> createResource(@RequestBody SongsDTO songsDTO) {
        log.info("--create resources--");
        return songsMetaDataService.save(songsDTO);
    }

    @DeleteMapping
    public ResponseEntity<Mp3IdListResponseDto> deleteResources(@RequestParam String id) {
        log.info("--delete resources--");
        return songsMetaDataService.deleteById(id);
    }

    @GetMapping
    public ResponseEntity<List<SongsDTO>> getResourcesByIds(@RequestParam List<Long> ids) {
        log.info("Fetching resources with IDs: {}", ids);
        List<SongsDTO> resources = songsMetaDataService.findByIds(ids);
        return ResponseEntity.ok(resources);
    }
}