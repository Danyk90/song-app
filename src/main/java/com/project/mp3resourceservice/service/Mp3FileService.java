package com.project.mp3resourceservice.service;

import com.project.mp3resourceservice.client.RestClient;
import com.project.mp3resourceservice.dto.Mp3FileResponseDto;
import com.project.mp3resourceservice.dto.Mp3MetadataDto;
import com.project.mp3resourceservice.entity.Mp3File;
import com.project.mp3resourceservice.mapper.Mp3FileMapper;
import com.project.mp3resourceservice.mapper.Mp3FileToResponseDtoMapper;
import com.project.mp3resourceservice.repo.Mp3FileRepository;
import com.project.mp3resourceservice.util.DurationFormatter;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.mp3.Mp3Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.helpers.DefaultHandler;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Mp3FileService {

    @Autowired
    private Mp3FileRepository mp3FileRepository;
    @Autowired
    private RestClient restClient;

    public Mp3FileResponseDto saveMp3File(MultipartFile file) throws Exception {
        Mp3MetadataDto metadataDto = new Mp3MetadataDto();
        metadataDto.setData(file.getBytes());

        // Extract metadata using Apache Tika
        InputStream input = new ByteArrayInputStream(file.getBytes());
        Metadata metadata = new Metadata();
        Mp3Parser parser = new Mp3Parser();
        parser.parse(input, new DefaultHandler(), metadata, new ParseContext());

        metadataDto.setTitle(metadata.get("title"));
        metadataDto.setArtist(metadata.get("xmpDM:artist"));
        metadataDto.setAlbum(metadata.get("xmpDM:album"));
        metadataDto.setName(metadata.get("resourceName"));

        metadataDto.setDuration(DurationFormatter.formatDuration(metadata.get("xmpDM:duration")));
        metadataDto.setYear(metadata.get("xmpDM:releaseDate"));
        Mp3File mp3File = Mp3File.create(metadataDto.getData());
        Mp3File savedFile = mp3FileRepository.save(mp3File);
        metadataDto.setId(savedFile.getId());
        Mp3MetadataDto mp3MetadataDto = Mp3FileMapper.toDto(savedFile);
        restClient.saveMetadata("http://localhost:8090/api/songs", mp3MetadataDto);
        // Save to database
        return Mp3FileToResponseDtoMapper.map(savedFile);
    }

    public Mp3MetadataDto findById(Long id) {

        mp3FileRepository.findById(id);
        return restClient.fetchMetadata("http://localhost:8090/api/songs", id);
    }

    public List<Long> deleteByIds(String ids) {
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        restClient.deleteMetadata("http://localhost:8090/api/songs", ids);
        mp3FileRepository.deleteAllById(idList);
        return idList;
    }
}
