package com.song.service.metadataservice;

import com.song.service.dto.Mp3IdListResponseDto;
import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;
import com.song.service.exception.CSVStringException;
import com.song.service.exception.ResourceAlreadyExistException;
import com.song.service.exception.ResourceNotFoundException;
import com.song.service.repo.SongsMetaDataRepository;
import com.song.service.util.MetaDataDtoToEntityMapper;
import com.song.service.util.SongsEntityToSongsDtoMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Validated
@Slf4j
public class SongsMetaDataService {

    private final SongsMetaDataRepository songsMetaDataRepository;

    public SongsMetaDataService(SongsMetaDataRepository songsMetaDataRepository) {
        this.songsMetaDataRepository = songsMetaDataRepository;
    }

    public SongsDTO findById(Long id) {

        Optional<SongsMetaDataEntity> songsMetaDataEntity = songsMetaDataRepository.findById(id);

        if (songsMetaDataEntity.isPresent()) {
            return SongsEntityToSongsDtoMapper.toDto(songsMetaDataEntity.get());
        } else {
            throw new ResourceNotFoundException("Song metadata for ID=" + id + " not found");
        }

    }

    public ResponseEntity<SongsDTO> save(@Valid SongsDTO songsDTO) {

        if (songsMetaDataRepository.findById(songsDTO.getId()).isPresent()) {
            throw new ResourceAlreadyExistException("Metadata for resource ID:" + songsDTO.getId() + " already exists.");
        }
        SongsMetaDataEntity songsMetaDataEntity = MetaDataDtoToEntityMapper.toEntity(songsDTO);
        songsMetaDataRepository.save(songsMetaDataEntity);
        log.info("Saved metadata for resource ID: {}", songsDTO.getId());
        return ResponseEntity.ok(songsDTO);
    }

    public ResponseEntity<Mp3IdListResponseDto> deleteById(String ids) {
        if (ids.length() > 200) {
            throw new CSVStringException("maximum allowed is 200");
        }
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .filter(id -> songsMetaDataRepository.existsById(id))
                .toList();
        songsMetaDataRepository.deleteAllById(idList);
        log.info("deleted metadata for resource IDs: {}", idList);
        Mp3IdListResponseDto mp3IdListResponseDto = new Mp3IdListResponseDto(idList);

        return ResponseEntity.ok(mp3IdListResponseDto);
    }

}