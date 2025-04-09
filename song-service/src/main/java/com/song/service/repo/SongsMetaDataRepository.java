package com.song.service.repo;


import com.song.service.entity.SongsMetaDataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongsMetaDataRepository extends JpaRepository<SongsMetaDataEntity, Long> {
}
