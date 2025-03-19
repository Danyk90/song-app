// src/main/java/com/song/service/processingservice/ResourcesService.java
package com.song.service.processingservice;

import com.song.service.dto.SongsDTO;
import com.song.service.entity.SongsMetaDataEntity;
import com.song.service.util.MetaDataDtoToEntityMapper;
import com.song.service.repo.SongsMetaDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SongsMetaDataService {

    @Autowired
    private SongsMetaDataRepository songsMetaDataRepository;

    public List<SongsMetaDataEntity> findAll() {
        return songsMetaDataRepository.findAll();
    }

    public Optional<SongsMetaDataEntity> findById(Long id) {
        return songsMetaDataRepository.findById(id);
    }

    public SongsMetaDataEntity save(SongsDTO songsDTO) {
        SongsMetaDataEntity songsMetaDataEntity = MetaDataDtoToEntityMapper.toEntity(songsDTO);
        return songsMetaDataRepository.save(songsMetaDataEntity);
    }

    public void deleteById(Long id) {
        songsMetaDataRepository.deleteById(id);
    }
}