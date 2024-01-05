package user.normalUser.search.bar;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import fileio.input.FiltersInput;
import org.checkerframework.checker.units.qual.A;
import user.artist.Artist;
import user.normalUser.NormalUser;

import java.util.ArrayList;

public final class SongFilter implements SearchFilter {
    private final String name;
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final String releaseYear;
    private final String artist;

    SongFilter(final FiltersInput filtersInput) {
        this.name = filtersInput.getName();
        this.album = filtersInput.getAlbum();
        this.tags = filtersInput.getTags();
        this.lyrics = filtersInput.getLyrics();
        this.genre = filtersInput.getGenre();
        this.releaseYear = filtersInput.getReleaseYear();
        this.artist = filtersInput.getArtist();
    }

    private boolean respectsTheFilter(final Song song) {
        if (name != null && !song.getName().toLowerCase().startsWith(name.toLowerCase())) {
            return false;
        }

        if (album != null && !song.getAlbum().equals(album)) {
            return false;
        }

        if (tags != null) {
            for (String tag : tags) {
                if (!song.getTags().contains(tag)) {
                    return false;
                }
            }
        }

        if (lyrics != null && !song.getLyrics().toLowerCase().contains(lyrics.toLowerCase())) {
            return false;
        }

        if (genre != null && !song.getGenre().equalsIgnoreCase(genre)) {
            return false;
        }

        if (releaseYear != null) {
            if (releaseYear.startsWith("<")) {
                if (song.getReleaseYear() >= Integer.parseInt(releaseYear.substring(1))) {
                    return false;
                }
            } else if (song.getReleaseYear()
                            <= Integer.parseInt(releaseYear.substring(1))) {
                return false;
            }
        }

        return artist == null || artist.equals(song.getArtist());
    }

    @Override
    public ArrayList<SearchableEntity> filter(final NormalUser normalUser) {
        int currentIndex = 0;
        ArrayList<SearchableEntity> filteredList = new ArrayList<>();

        for (Song song : Admin.getSongs()) {
            if (respectsTheFilter(song)) {
                    filteredList.add(song);
                    currentIndex++;
                    if (currentIndex == MAX_FILTERED_LIST_SIZE) {
                        return filteredList;
                    }
                }
        }

        return filteredList;
    }
}
