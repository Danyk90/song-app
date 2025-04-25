package com.project.resourceservice.service;

import com.project.resourceservice.client.SongServiceClient;
import com.project.resourceservice.config.SongServiceProperties;
import com.project.resourceservice.dto.Mp3FileResponseDto;
import com.project.resourceservice.dto.Mp3IdListResponseDto;
import com.project.resourceservice.dto.Mp3MetadataDto;
import com.project.resourceservice.entity.Mp3File;
import com.project.resourceservice.exception.CSVStringException;
import com.project.resourceservice.exception.InvalidFileFormatException;
import com.project.resourceservice.exception.ResourceNotFoundException;
import com.project.resourceservice.mapper.Mp3FileToResponseDtoMapper;
import com.project.resourceservice.repo.Mp3FileRepository;
import com.project.resourceservice.util.DurationFormatter;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class Mp3FileService {

    private static final Logger log = LoggerFactory.getLogger(Mp3FileService.class);
    private final Mp3FileRepository mp3FileRepository;
    //private final RestClient restClient;
    private final SongServiceClient songServiceClient;
    private final SongServiceProperties songServiceProperties;

    public Mp3FileService(Mp3FileRepository mp3FileRepository, SongServiceClient feignClient, SongServiceProperties songServiceProperties) {
        this.mp3FileRepository = mp3FileRepository;
        this.songServiceClient = feignClient;
        this.songServiceProperties = songServiceProperties;
    }

    private void validateMp3File(byte[] file) throws IOException {
        // Check if the file is empty
        if (file == null || file.length == 0) {
            throw new InvalidFileFormatException("File is empty");
        }

        // Use Apache Tika to validate MP3 content
        try (InputStream input = new ByteArrayInputStream(file)) {
            Metadata metadata = new Metadata();
            Mp3Parser parser = new Mp3Parser();
            parser.parse(input, new DefaultHandler(), metadata, new ParseContext());

            // Optionally, check for specific metadata fields
            if (metadata.get("xmpDM:duration") == null) {
                throw new InvalidFileFormatException("Invalid MP3 file content");
            }
            if (metadata.get("dc:title") == null) {
                throw new InvalidFileFormatException("Invalid MP3 file content");
            }
            if (metadata.get("xmpDM:artist") == null) {
                throw new InvalidFileFormatException("Invalid MP3 file content");
            }
            if (metadata.get("xmpDM:album") == null) {
                throw new InvalidFileFormatException("Invalid MP3 file content");
            }
            if (metadata.get("xmpDM:releaseDate") == null) {
                throw new InvalidFileFormatException("Invalid MP3 file content");
            }
        } catch (TikaException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public Mp3FileResponseDto saveMp3File(byte[] file) throws IOException, TikaException, SAXException {

        validateMp3File(file);
        Mp3MetadataDto metadataDto = new Mp3MetadataDto();
        metadataDto.setData(file);

        // Extract metadata using Apache Tika
        InputStream input = new ByteArrayInputStream(file);
        Metadata metadata = new Metadata();
        Mp3Parser parser = new Mp3Parser();
        parser.parse(input, new DefaultHandler(), metadata, new ParseContext());

        metadataDto.setTitle(metadata.get("dc:title"));
        metadataDto.setArtist(metadata.get("xmpDM:artist"));
        metadataDto.setAlbum(metadata.get("xmpDM:album"));
        metadataDto.setName(metadata.get("dc:title") + "." + metadata.get("xmpDM:audioCompressor").toLowerCase());

        metadataDto.setDuration(DurationFormatter.formatDuration(metadata.get("xmpDM:duration")));
        metadataDto.setYear(metadata.get("xmpDM:releaseDate"));
        Mp3File mp3File = Mp3File.create(metadataDto.getData());
        Mp3File savedFile = mp3FileRepository.save(mp3File);
        metadataDto.setId(savedFile.getId());
        log.info("-----songserviceurl--" + songServiceProperties.getUrl());
        songServiceClient.saveResource(metadataDto);
        //       restClient.saveSongMetadata(songServiceProperties.getUrl(), metadataDto);
        return Mp3FileToResponseDtoMapper.mapToResponseDto(savedFile);
    }

    public ResponseEntity<byte[]> findById(Long id) {
        if (id <= 0) {
            throw new MethodArgumentTypeMismatchException(id, Long.class, "ID", null, new IllegalArgumentException());
        }
        Optional<Mp3File> mp3File = mp3FileRepository.findById(id);
        if (mp3File.isPresent()) {
            log.info("entity found with ID: {}", id);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_LENGTH,
                            String.valueOf(mp3File.get().getData().length))
                    .body((mp3File.get().getData()));
        }
        throw new ResourceNotFoundException(id.toString());
    }

    public ResponseEntity<Mp3IdListResponseDto> deleteByIds(String ids) {
        if (ids.length() > 200) {
            throw new CSVStringException("maximum allowed is 200");
        }
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .filter(id -> mp3FileRepository.existsById(id))
                .toList();
        songServiceClient.deleteResource(ids);
//        restClient.deleteSongMetadata(songServiceProperties.getUrl(), ids);
        mp3FileRepository.deleteAllById(idList);
        Mp3IdListResponseDto mp3IdListResponseDto = new Mp3IdListResponseDto(idList);
        return ResponseEntity.ok(mp3IdListResponseDto);
    }
}
