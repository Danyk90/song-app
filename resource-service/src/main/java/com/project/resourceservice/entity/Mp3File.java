package com.project.resourceservice.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;

import java.sql.Types;


@Data
@NoArgsConstructor
@Entity(name = "mp3_file")
public class Mp3File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @JdbcTypeCode(Types.VARBINARY)
    @Column(name = "data")
    private byte[] data;

    public Mp3File(byte[] data) {
        this.data = data;
    }

    public static Mp3File create(byte[] data) {
        return new Mp3File(data);
    }
}