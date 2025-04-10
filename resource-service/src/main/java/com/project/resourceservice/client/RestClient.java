package com.project.resourceservice.client;


import com.project.resourceservice.dto.Mp3MetadataDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestClient {

    private final RestTemplate restTemplate;

    public RestClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void saveSongMetadata(String url, Mp3MetadataDto mp3FileMetadata) {
        restTemplate.postForObject(url, mp3FileMetadata, Mp3MetadataDto.class);
    }

    public void deleteSongMetadata(String url, String ids) {
        restTemplate.delete(url + "?id=" + ids);
    }
}