package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.exceptions.AudioStreamingFailedException;
import dev.semkoksharov.vibeshub2.exceptions.FilePathIsNullException;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class AudioStreamingService implements dev.semkoksharov.vibeshub2.service.interfaces.AudioStreamingServiceInt {

    private final MinioClient minioClient;
    private final SongRepo songRepository;

    @Value("${minio.musicBucket}")
    private String musicBucket;

    @Autowired
    public AudioStreamingService(MinioClient minioClient, SongRepo songRepository) {
        this.minioClient = minioClient;
        this.songRepository = songRepository;
    }

    @Override
    public void streamAudio(Long songID, HttpServletResponse response) {
        String filepath = findAudio2Stream(songID);

        if (filepath == null) {
            throw new FilePathIsNullException("[Streaming service error] Filepath is null.");
        }

        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(musicBucket)
                        .object(filepath)
                        .build())) {

            response.setContentType("audio/mpeg");
            response.setHeader("Accept-Ranges", "bytes");

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();

        } catch (IOException | ServerException | InsufficientDataException | NoSuchAlgorithmException | ErrorResponseException |
                 InvalidKeyException | InvalidResponseException | XmlParserException | InternalException e) {
            throw new AudioStreamingFailedException("[Streaming service error] File: " + filepath + " Caused by: " + e.getClass() + " Error message: " + e.getMessage());
        }
    }

    private String findAudio2Stream(Long songID) {
        Song song2Stream = songRepository.findById(songID)
                .orElseThrow(() -> new EntityNotFoundException("[Streaming error] Audio file with id " + songID + " not found."));

        return song2Stream.getMinioPath();
    }
}
