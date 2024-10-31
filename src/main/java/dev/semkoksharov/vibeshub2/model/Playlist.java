package dev.semkoksharov.vibeshub2.model;

import dev.semkoksharov.vibeshub2.model.base.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.LinkedList;
import java.util.Set;
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "playlists")
public class Playlist extends BaseEntity {

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = UserEntity.class)
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String title;

    @ManyToMany
    @JoinTable(
            name = "playlist_songs",
            joinColumns = @JoinColumn(name = "playlist_id"),
            inverseJoinColumns = @JoinColumn(name = "song_id")
    )
    private LinkedList<Song> songs;

    public void addSongToPlaylist(Song song){
        this.songs.add(song);
    }

    public void deleteSongFromPlaylist(Song song){
        this.songs.remove(song);
    }

}
