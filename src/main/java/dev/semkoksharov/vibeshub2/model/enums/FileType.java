package dev.semkoksharov.vibeshub2.model.enums;

import java.util.Set;

public enum FileType {

    AUDIO(Set.of("mp3", "wav", "flac", "m4a")),
    ALBUM_COVER(Set.of("jpg", "jpeg", "png", "gif")),
    PROFILE_PICTURE(Set.of("jpg", "jpeg", "png", "gif"));

    public final Set<String> validExtensions;

    FileType(Set<String> validExtensions) {
        this.validExtensions = validExtensions;
    }
}