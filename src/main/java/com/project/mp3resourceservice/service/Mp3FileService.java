package com.project.mp3resourceservice.service;

import com.project.mp3resourceservice.client.RestClient;
import com.project.mp3resourceservice.dto.Mp3FileResponseDto;
import com.project.mp3resourceservice.dto.Mp3IdListResponseDto;
import com.project.mp3resourceservice.dto.Mp3MetadataDto;
import com.project.mp3resourceservice.entity.Mp3File;
import com.project.mp3resourceservice.exception.CSVStringException;
import com.project.mp3resourceservice.exception.InvalidFileFormatException;
import com.project.mp3resourceservice.exception.ResourceNotFoundException;
import com.project.mp3resourceservice.exception.RestClientException;
import com.project.mp3resourceservice.mapper.Mp3FileToResponseDtoMapper;
import com.project.mp3resourceservice.repo.Mp3FileRepository;
import com.project.mp3resourceservice.util.DurationFormatter;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class Mp3FileService {

    @Autowired
    private Mp3FileRepository mp3FileRepository;
    @Autowired
    private RestClient restClient;

    private void validateMp3File(MultipartFile file) {
        if (file.isEmpty() || !Objects.requireNonNull(file.getOriginalFilename()).endsWith(".mp3")) {
            throw new InvalidFileFormatException("Invalid MP3 file format");
        }
    }

    public Mp3FileResponseDto saveMp3File(MultipartFile file) throws IOException, TikaException, SAXException {
        validateMp3File(file);
        Mp3MetadataDto metadataDto = new Mp3MetadataDto();
        metadataDto.setData(file.getBytes());

        // Extract metadata using Apache Tika
        InputStream input = new ByteArrayInputStream(file.getBytes());
        Metadata metadata = new Metadata();
        Mp3Parser parser = new Mp3Parser();
        parser.parse(input, new DefaultHandler(), metadata, new ParseContext());

        metadataDto.setTitle(metadata.get("dc:title"));
        metadataDto.setArtist(metadata.get("xmpDM:artist"));
        metadataDto.setAlbum(metadata.get("xmpDM:album"));
        metadataDto.setName(file.getOriginalFilename());

        metadataDto.setDuration(DurationFormatter.formatDuration(metadata.get("xmpDM:duration")));
        metadataDto.setYear(metadata.get("xmpDM:releaseDate"));
        Mp3File mp3File = Mp3File.create(metadataDto.getData());
        Mp3File savedFile = mp3FileRepository.save(mp3File);
        metadataDto.setId(savedFile.getId());
        // Mp3MetadataDto mp3MetadataDto = Mp3FileMapper.toDto(savedFile);
        // restClient.saveSongMetadata("http://localhost:8090/songs", metadataDto);
        // Save to database
        try {
            restClient.saveSongMetadata("http://localhost:8090/songs", metadataDto);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            throw new RestClientException("Error from RestClient: " + e.getResponseBodyAsString(),
                    e.getStatusCode().value(),
                    e.getResponseBodyAsString());
        }
        return Mp3FileToResponseDtoMapper.map(savedFile);
    }

    public ResponseEntity<?> findById(Long id) {
        if (id <= 0) {
            throw new MethodArgumentTypeMismatchException(id, Long.class, "id", null, new IllegalArgumentException());
        }
        Optional<Mp3File> mp3File = mp3FileRepository.findById(id);
        if (mp3File.isPresent()) {

            return ResponseEntity.ok(mp3File.get().getData());
            //return restClient.fetchSongMetadata("http://localhost:8090/songs", id);
        }
        throw new ResourceNotFoundException("Resource with id:" + id + " not found");
    }

    public ResponseEntity<Mp3IdListResponseDto> deleteByIds(String ids) {
        if (ids.length() > 200) {
            throw new CSVStringException("maximum allowed is 200");
        }
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .filter(id -> mp3FileRepository.existsById(id))
                .toList();
        restClient.deleteSongMetadata("http://localhost:8090/songs", ids);
        mp3FileRepository.deleteAllById(idList);
        Mp3IdListResponseDto mp3IdListResponseDto = new Mp3IdListResponseDto(idList);
        return ResponseEntity.ok(mp3IdListResponseDto);
    }
}
