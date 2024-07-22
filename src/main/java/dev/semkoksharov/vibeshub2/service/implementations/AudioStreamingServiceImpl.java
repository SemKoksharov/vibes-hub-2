package dev.semkoksharov.vibeshub2.service.implementations;

import dev.semkoksharov.vibeshub2.exceptions.AudioStreamingFailedException;
import dev.semkoksharov.vibeshub2.exceptions.FilePathIsNullException;
import dev.semkoksharov.vibeshub2.model.Song;
import dev.semkoksharov.vibeshub2.repository.SongRepo;
import dev.semkoksharov.vibeshub2.service.interfaces.AudioStreamingService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
public class AudioStreamingServiceImpl implements AudioStreamingService {

    private final MinioClient minioClient;
    private final SongRepo songRepository;
    private final Tika tika = new Tika();

    @Value("${minio.musicBucket}")
    private String musicBucket;

    @Autowired
    public AudioStreamingServiceImpl(MinioClient minioClient, SongRepo songRepository) {
        this.minioClient = minioClient;
        this.songRepository = songRepository;
    }

    @Override
    public void streamAudio(Long songID, HttpServletRequest request, HttpServletResponse response) {
        String filepath = findAudio2Stream(songID);

        if (filepath == null) {
            throw new FilePathIsNullException("[Streaming service error] Filepath is null.");
        }

        try (InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(musicBucket)
                        .object(filepath)
                        .build())) {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            IOUtils.copy(inputStream, buffer);
            byte[] data = buffer.toByteArray();

            String contentType = tika.detect(data);
            response.setContentType(contentType);
            response.setHeader("Accept-Ranges", "bytes");

            String range = request.getHeader("Range");
            if (range != null) {
                long fileLength = data.length;
                long start = 0, end = fileLength - 1;

                String[] ranges = range.replace("bytes=", "").split("-");
                try {
                    if (ranges.length > 0) {
                        start = Long.parseLong(ranges[0]);
                    }
                    if (ranges.length > 1) {
                        end = Long.parseLong(ranges[1]);
                    }
                } catch (NumberFormatException e) {
                    response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return;
                }

                if (start >= fileLength || end >= fileLength) {
                    response.setStatus(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                    return;
                }

                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                long contentLength = end - start + 1;
                response.setHeader("Content-Length", String.valueOf(contentLength));
                response.setHeader("Content-Range", "bytes " + start + "-" + end + "/" + fileLength);

                try (InputStream byteArrayInputStream = new ByteArrayInputStream(data, (int) start, (int) contentLength)) {
                    IOUtils.copy(byteArrayInputStream, response.getOutputStream());
                    response.flushBuffer();
                }
            } else {
                response.setHeader("Content-Length", String.valueOf(data.length));
                try (InputStream byteArrayInputStream = new ByteArrayInputStream(data)) {
                    IOUtils.copy(byteArrayInputStream, response.getOutputStream());
                    response.flushBuffer();
                }
            }

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

    private String getExtension(String contentType) {
        return switch (contentType) {
            case "audio/mpeg" -> "mp3";
            case "audio/wav", "audio/vnd.wave" -> "wav";
            case "audio/mp4" -> "m4a";
            case "audio/flac" -> "flac";
            default -> "bin";
        };
    }
}
