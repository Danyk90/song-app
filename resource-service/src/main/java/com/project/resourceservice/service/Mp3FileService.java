package com.project.resourceservice.service;

import com.project.resourceservice.client.RestClient;
import com.project.resourceservice.config.SongServiceProperties;
import com.project.resourceservice.dto.Mp3FileResponseDto;
import com.project.resourceservice.dto.Mp3IdListResponseDto;
import com.project.resourceservice.dto.Mp3MetadataDto;
import com.project.resourceservice.entity.Mp3File;
import com.project.resourceservice.exception.CSVStringException;
import com.project.resourceservice.exception.InvalidFileFormatException;
import com.project.resourceservice.exception.ResourceNotFoundException;
import com.project.resourceservice.exception.RestClientException;
import com.project.resourceservice.mapper.Mp3FileToResponseDtoMapper;
import com.project.resourceservice.repo.Mp3FileRepository;
import com.project.resourceservice.util.DurationFormatter;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private static final Logger log = LoggerFactory.getLogger(Mp3FileService.class);

    private final Mp3FileRepository mp3FileRepository;
    private final RestClient restClient;
    private final SongServiceProperties songServiceProperties;

    public Mp3FileService(Mp3FileRepository mp3FileRepository, RestClient restClient, SongServiceProperties songServiceProperties) {
        this.mp3FileRepository = mp3FileRepository;
        this.restClient = restClient;
        this.songServiceProperties = songServiceProperties;
    }

    private void validateMp3File(byte[] file) throws IOException {
        // Check if the file is empty
            if (file == null || file.length == 0) {
                throw new InvalidFileFormatException("File is empty");
            }

            // Check file signature (magic numbers)
           /* if (file.length < 2 || (file[0] & 0xFF) != 0xFF || (file[1] & 0xE0) != 0xE0) {
                throw new InvalidFileFormatException("Invalid MP3 file format");
            }*/

            // Use Apache Tika to validate MP3 content
            try (InputStream input = new ByteArrayInputStream(file)) {
                Metadata metadata = new Metadata();
                Mp3Parser parser = new Mp3Parser();
                parser.parse(input, new DefaultHandler(), metadata, new ParseContext());

                // Optionally, check for specific metadata fields
                if (metadata.get("xmpDM:duration") == null) {
                    throw new InvalidFileFormatException("Invalid MP3 file content");
                }
            } catch (TikaException e) {
                throw new RuntimeException(e);
            } catch (SAXException e) {
                throw new RuntimeException(e);
            }
    }

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
        //metadataDto.setName(file.toString().);

        metadataDto.setDuration(DurationFormatter.formatDuration(metadata.get("xmpDM:duration")));
        metadataDto.setYear(metadata.get("xmpDM:releaseDate"));
        Mp3File mp3File = Mp3File.create(metadataDto.getData());
        Mp3File savedFile = mp3FileRepository.save(mp3File);
        metadataDto.setId(savedFile.getId());
        log.info("--songserviceurl--" + songServiceProperties.getUrl());
        restClient.saveSongMetadata(songServiceProperties.getUrl(), metadataDto);
        return Mp3FileToResponseDtoMapper.mapToResponseDto(savedFile);
    }

    public ResponseEntity<byte[]> findById(Long id) {
        if (id <= 0) {
            throw new MethodArgumentTypeMismatchException(id, Long.class, "id", null, new IllegalArgumentException());
        }
        Optional<Mp3File> mp3File = mp3FileRepository.findById(id);
        if (mp3File.isPresent()) {

            return ResponseEntity.ok(mp3File.get().getData());
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
        restClient.deleteSongMetadata(songServiceProperties.getUrl(), ids);
        mp3FileRepository.deleteAllById(idList);
        Mp3IdListResponseDto mp3IdListResponseDto = new Mp3IdListResponseDto(idList);
        return ResponseEntity.ok(mp3IdListResponseDto);
    }
}
