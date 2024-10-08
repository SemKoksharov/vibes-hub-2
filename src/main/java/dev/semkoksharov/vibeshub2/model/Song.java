package dev.semkoksharov.vibeshub2.model;

import dev.semkoksharov.vibeshub2.interfaces.Uploadable;
import dev.semkoksharov.vibeshub2.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "songs")
public class Song extends BaseEntity implements  Uploadable{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id")
    private Album album;
    private int duration;
    private String title;
    /////////////////////////////////////////////////////////////////////////////////
    // These fields below refer to files in blob storage(in this case an audio file)
    @Lob
    @Column(columnDefinition="TEXT", length = 1000)
    private String directUrl;
    private String streamingUrl;
    private String minioPath;                                                      //
    ////////////////////////////////////////////////////////////////////////////////
}