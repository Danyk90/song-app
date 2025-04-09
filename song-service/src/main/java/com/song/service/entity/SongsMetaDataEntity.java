package com.song.service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "songs_metadata")
@Data
public class SongsMetaDataEntity {
    @Id
    private Long id;
    private String name;
    private String artist;
    private String album;
    private String duration;
    @Column(name = "\"year\"")
    private String year;
}