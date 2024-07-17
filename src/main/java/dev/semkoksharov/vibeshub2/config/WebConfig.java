package dev.semkoksharov.vibeshub2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        configurer
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("png", MediaType.IMAGE_PNG)
                .mediaType("jpg", MediaType.IMAGE_JPEG)
                .mediaType("jpeg", MediaType.IMAGE_JPEG)
                .mediaType("gif", MediaType.IMAGE_GIF)
                .mediaType("mp3", MediaType.valueOf("audio/mpeg"))
                .mediaType("wav", MediaType.valueOf("audio/wav"))
                .mediaType("m4a", MediaType.valueOf("audio/mp4"))
                .mediaType("flac", MediaType.valueOf("audio/flac"))
                .mediaType("multipart", MediaType.MULTIPART_FORM_DATA);
    }
}
