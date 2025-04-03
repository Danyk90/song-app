package com.song.service.controller;

import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;
import com.song.service.processingservice.SongsMetaDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<SongsMetaDataEntity> getResourceById(@PathVariable Long id) {
        Optional<SongsMetaDataEntity> resource = songsMetaDataService.findById(id);
        return resource.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createResource(@RequestBody SongsDTO songsDTO) {
        if (songsMetaDataService.findById(songsDTO.getId()).isPresent()) {
            return ResponseEntity.status(409).body("Metadata for this ID already exists.");
        }
        SongsMetaDataEntity createdEntity = songsMetaDataService.save(songsDTO);
        return ResponseEntity.ok(createdEntity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SongsMetaDataEntity> updateResource(@PathVariable Long id, @RequestBody SongsDTO songsDTO) {
        if (songsMetaDataService.findById(id).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        songsDTO.setId(id);
        return ResponseEntity.ok(songsMetaDataService.save(songsDTO));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteResources(@RequestParam String id) {
        if (id.length() > 200) {
            return ResponseEntity.badRequest().body("CSV string length exceeds 200 characters.");
        }

        String[] idArray = id.split(",");
        List<Long> deletedIds = new ArrayList<>();

        for (String idStr : idArray) {
            try {
                Long idLong = Long.parseLong(idStr.trim());
                if (songsMetaDataService.findById(idLong).isPresent()) {
                    songsMetaDataService.deleteById(idLong);
                    deletedIds.add(idLong);
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.badRequest().body("Invalid ID format: " + idStr);
            }
        }

        return ResponseEntity.ok(Collections.singletonMap("ids", deletedIds));
    }
}