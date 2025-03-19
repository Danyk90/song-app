package com.project.mp3resourceservice.entity;

import jakarta.persistence.*;

@Entity
public class Mp3File {

    protected Mp3File(byte[] data) {
        this.data = data;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Lob
    private byte[] data;

    public static Mp3File create(byte[] data) {
       return new Mp3File(data);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}