package com.project.mp3resourceservice.controller;

import com.project.mp3resourceservice.dto.Mp3FileResponseDto;
import com.project.mp3resourceservice.dto.Mp3IdListResponseDto;
import com.project.mp3resourceservice.service.Mp3FileService;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import javax.validation.Valid;
import java.io.IOException;

@RestController
@RequestMapping("/resources")
public class Mp3FileController {

    @Autowired
    private Mp3FileService mp3FileService;

    @PostMapping
    public ResponseEntity<?> uploadMp3File(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {

        Mp3FileResponseDto mp3File = mp3FileService.saveMp3File(file);
        return ResponseEntity.ok(mp3File);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMp3FileById(@Valid @PathVariable Long id) {
        return mp3FileService.findById(id);

    }

    @DeleteMapping
    public ResponseEntity<Mp3IdListResponseDto> deleteMp3Files(@RequestParam("id") String ids) {

        return mp3FileService.deleteByIds(ids);
    }
}
