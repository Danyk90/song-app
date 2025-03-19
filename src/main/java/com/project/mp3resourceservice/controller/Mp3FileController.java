package com.project.mp3resourceservice.controller;

import com.project.mp3resourceservice.dto.Mp3FileResponseDto;
import com.project.mp3resourceservice.dto.Mp3MetadataDto;
import com.project.mp3resourceservice.entity.Mp3File;
import com.project.mp3resourceservice.service.Mp3FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/resources")
public class Mp3FileController {

    @Autowired
    private Mp3FileService mp3FileService;

    @PostMapping
    public ResponseEntity<?> uploadMp3File(@RequestParam("file") MultipartFile file) {
        try {

             Mp3FileResponseDto mp3File = mp3FileService.saveMp3File(file);
             return ResponseEntity.ok(mp3File);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getMp3FileById(@PathVariable Long id) {
        Mp3MetadataDto mp3File = mp3FileService.findById(id);
        if (mp3File == null) {
            return ResponseEntity.status(404).build();
        }
        return ResponseEntity.ok(mp3File.getData());
    }

    @DeleteMapping
    public ResponseEntity<List<Long>> deleteMp3Files(@RequestParam("id") String ids) {

        List<Long> idList = mp3FileService.deleteByIds(ids);
        return ResponseEntity.ok(idList);
    }
}
