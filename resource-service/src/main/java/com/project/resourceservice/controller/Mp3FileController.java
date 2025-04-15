package com.project.resourceservice.controller;

import com.project.resourceservice.dto.Mp3FileResponseDto;
import com.project.resourceservice.dto.Mp3IdListResponseDto;
import com.project.resourceservice.service.Mp3FileService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.exception.TikaException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import java.io.IOException;

@RestController
@RequestMapping("/resources")
@Slf4j
public class Mp3FileController {

    private final Mp3FileService mp3FileService;

    public Mp3FileController(Mp3FileService mp3FileService) {
        this.mp3FileService = mp3FileService;
    }

    @PostMapping(consumes = "audio/mpeg")
    public ResponseEntity<Mp3FileResponseDto> uploadMp3File(@RequestBody byte[] fileData) throws IOException, TikaException, SAXException {
        log.info("-------uploading mp3 file");
        Mp3FileResponseDto mp3File = mp3FileService.saveMp3File(fileData);
        return ResponseEntity.ok(mp3File);
    }

    @GetMapping(path = "/{id}", produces = "audio/mpeg")
    public ResponseEntity<byte[]> getMp3FileById(@PathVariable Long id) {
        log.info("-------searching for resource with ID: {}", id);
        return mp3FileService.findById(id);
    }

    @DeleteMapping
    public ResponseEntity<Mp3IdListResponseDto> deleteMp3Files(@RequestParam("id") String ids) {
        log.info("-------Deleting resources with IDs: {}", ids);
        return mp3FileService.deleteByIds(ids);
    }
}
