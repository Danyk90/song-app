package com.project.mp3resourceservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Entity
@NoArgsConstructor
public class Mp3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Lob
    private byte[] data;

    public Mp3File(byte[] data) {
        this.data = data;
    }

    public static Mp3File create(byte[] data) {
        return new Mp3File(data);
    }
}