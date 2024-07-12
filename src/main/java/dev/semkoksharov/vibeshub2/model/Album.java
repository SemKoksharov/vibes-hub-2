package dev.semkoksharov.vibeshub2.model;

import dev.semkoksharov.vibeshub2.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "albums")
public class Album extends BaseEntity {

    private String title;
    private String coverPhotoUrl;
    private String minioCoverPath;
    private int year;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Artist> artists = new HashSet<>();

    @OneToMany(mappedBy = "album", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Song> songs = new HashSet<>();

    public void addSong(Song song) {
        this.songs.add(song);
        song.setAlbum(this);
    }

    public void removeSong(Song song) {
        this.songs.remove(song);
        song.setAlbum(null);
    }

    public void addArtist(Artist artist) {
        this.artists.add(artist);
    }

    public void removeArtist(Artist artist) {
        this.artists.remove(artist);
    }
}
