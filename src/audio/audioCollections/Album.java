package audio.audioCollections;

import audio.audioFiles.Song;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public final class Album extends SongsCollection {
    private final int releaseYear;
    private final String description;

    public Album(final String name1,
                 final String owner1,
                 final int releaseYear1,
                 final String description1,
                 final ArrayList<Song> songs1) {
        super(name1, owner1);
        releaseYear = releaseYear1;
        description = description1;
        songs = songs1;
    }

    @Override
    public AudioType getType() {
        return AudioType.ALBUM;
    }
}
