package com.song.service.controller;

import com.song.service.dto.Mp3IdListResponseDto;
import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;
import com.song.service.exception.CSVStringException;
import com.song.service.metadataservice.SongsMetaDataService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/songs")
public class SongServiceController {

    @Autowired
    private SongsMetaDataService songsMetaDataService;

    @GetMapping("/{id}")
    public ResponseEntity<SongsDTO> getResourceById(@PathVariable @Valid Long id) {
        log.info("Fetching resource with ID: {}", id);

        Optional<SongsDTO> resource = Optional.of(songsMetaDataService.findById(id));
        return resource.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<SongsDTO> createResource(@Valid @RequestBody SongsDTO songsDTO) {
        log.info("--create resources--");
        return songsMetaDataService.save(songsDTO);
    }

    @DeleteMapping
    public ResponseEntity<Mp3IdListResponseDto> deleteResources(@RequestParam @Valid String id) {
        log.info("--delete resources--");
        return songsMetaDataService.deleteById(id);
    }
}