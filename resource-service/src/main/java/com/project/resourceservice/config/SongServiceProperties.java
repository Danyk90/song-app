package com.project.resourceservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "song.service")
@Data
public class SongServiceProperties {

    /**
     * URL for communicating with song-service
     */
    private String url;
}
