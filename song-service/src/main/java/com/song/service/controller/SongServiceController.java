package com.song.service.controller;

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

    @GetMapping
    public List<SongsMetaDataEntity> getAllResources() {
        return songsMetaDataService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongsMetaDataEntity> getResourceById(@PathVariable @Valid Long id) {
        Optional<SongsMetaDataEntity> resource = songsMetaDataService.findById(id);
        return resource.map(res -> ResponseEntity.ok(res)).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createResource(@Valid @RequestBody SongsDTO songsDTO) {

        return songsMetaDataService.save(songsDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateResource(@PathVariable @Valid Long id, @RequestBody @Valid SongsDTO songsDTO) {

        return songsMetaDataService.save(songsDTO);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResources(@RequestParam @Valid String id) {
        log.info("--delete resources--");
        if (id.length() > 200) {
            throw new CSVStringException("maximum allowed is 200");
        }
        return songsMetaDataService.deleteById(id);
    }
}