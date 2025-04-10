package com.song.service.metadataservice;

import com.song.service.dto.Mp3IdListResponseDto;
import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;
import com.song.service.exception.CSVStringException;
import com.song.service.exception.ResourceAlreadyExistException;
import com.song.service.exception.ResourceNotFoundException;
import com.song.service.util.MetaDataDtoToEntityMapper;
import com.song.service.repo.SongsMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SongsMetaDataService {

    private final SongsMetaDataRepository songsMetaDataRepository;

    public SongsMetaDataService(SongsMetaDataRepository songsMetaDataRepository) {
        this.songsMetaDataRepository = songsMetaDataRepository;
    }

    public List<SongsMetaDataEntity> findAll() {
        return songsMetaDataRepository.findAll();
    }

    public Optional<SongsMetaDataEntity> findById(Long id) {

        Optional<SongsMetaDataEntity> songsMetaDataEntity = songsMetaDataRepository.findById(id);

        if (songsMetaDataEntity.isPresent()) {
            return songsMetaDataEntity;
        } else {
            throw new ResourceNotFoundException("Song metadata for ID=" + id + " not found");
        }

    }

    public ResponseEntity<?> save(SongsDTO songsDTO) {
        //  SongMetadataRequestValidator.validate(songsDTO);

        if (songsMetaDataRepository.findById(songsDTO.getId()).isPresent()) {
            throw new ResourceAlreadyExistException("Metadata for resource ID:" + songsDTO.getId() + " already exists.");
        }
        SongsMetaDataEntity songsMetaDataEntity = MetaDataDtoToEntityMapper.toEntity(songsDTO);
        return ResponseEntity.ok(songsMetaDataRepository.save(songsMetaDataEntity));
    }

    public ResponseEntity<?> deleteById(String ids) {
        if (ids.length() > 200) {
            throw new CSVStringException("maximum allowed is 200");
        }
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .filter(id -> songsMetaDataRepository.existsById(id))
                .toList();
        songsMetaDataRepository.deleteAllById(idList);
        Mp3IdListResponseDto mp3IdListResponseDto = new Mp3IdListResponseDto(idList);

        return ResponseEntity.ok(mp3IdListResponseDto);
    }

}