package user.normalUser.page;

import audio.audioCollections.Playlist;
import audio.audioFiles.Song;
import user.normalUser.NormalUser;

import java.util.ArrayList;

public final class HomePage implements Page {
    private static final int MAX_ARRAY_LENGTH = 5;
    private final NormalUser normalUser;

    public HomePage(final NormalUser normalUser1) {
        normalUser = normalUser1;
    }

    @Override
    public String printPage() {
        final StringBuilder builder = new StringBuilder("Liked songs:\n\t[");

        ArrayList<Song> bestSongs = new ArrayList<>(normalUser.getLikedSongs());
        bestSongs.sort((song1, song2) -> song2.getLikes().size() - song1.getLikes().size());

        if (!bestSongs.isEmpty()) {
            builder.append(bestSongs.get(0).getName());
        }
        for (int songIndex = 1;
             songIndex < MAX_ARRAY_LENGTH && songIndex < bestSongs.size();
             songIndex++) {
            builder.append(", ").append(bestSongs.get(songIndex).getName());
        }

        builder.append("]\n\nFollowed playlists:\n\t[");

        ArrayList<Playlist> bestPlaylists = new ArrayList<>(normalUser.getFollowedPlaylists());
        bestPlaylists.sort((playlist1, playlist2) ->
                playlist2.getNoFollowers() - playlist1.getNoFollowers());
        if (!bestPlaylists.isEmpty()) {
            builder.append(bestPlaylists.get(0).getName());
        }
        for (int playlistIndex = 1;
             playlistIndex < bestPlaylists.size() && playlistIndex < MAX_ARRAY_LENGTH;
             playlistIndex++) {
            builder.append(", ")
                    .append(bestPlaylists.get(playlistIndex).getName());
        }
        builder.append("]");

        return builder.toString();
    }
}
