package user.normalUser.page;

import audio.audioCollections.Playlist;
import audio.audioFiles.Song;
import user.normalUser.NormalUser;
import user.normalUser.player.PlayableEntity;

import java.util.ArrayList;

public final class HomePage implements Page {
    private static final int MAX_ARRAY_LENGTH = 5;
    private final NormalUser normalUser;

    public HomePage(final NormalUser normalUser1) {
        normalUser = normalUser1;
    }

    private static void appendArray(final StringBuilder builder,
                                 final ArrayList<? extends PlayableEntity> array) {
        if (!array.isEmpty()) {
            builder.append(array.get(0).getName());
        }
        for (int index = 1;
             index < MAX_ARRAY_LENGTH && index < array.size();
             index++) {
            builder.append(", ").append(array.get(index).getName());
        }
    }

    @Override
    public String printPage() {
        final StringBuilder builder = new StringBuilder("Liked songs:\n\t[");

        ArrayList<Song> bestSongs = new ArrayList<>(normalUser.getLikedSongs());
        bestSongs.sort((song1, song2) -> song2.getLikes().size() - song1.getLikes().size());
        appendArray(builder, bestSongs);

        builder.append("]\n\nFollowed playlists:\n\t[");
        ArrayList<Playlist> bestPlaylists = new ArrayList<>(normalUser.getFollowedPlaylists());
        bestPlaylists.sort((playlist1, playlist2) ->
                playlist2.getNoFollowers() - playlist1.getNoFollowers());
        appendArray(builder, bestPlaylists);

        builder.append("]\n\nSong recommendations:\n\t[");
        appendArray(builder, normalUser.getRecommendedSongs());

        builder.append("]\n\nPlaylists recommendations:\n\t[");
        appendArray(builder, normalUser.getRecommendedPlaylists());

        builder.append("]");
        return builder.toString();
    }
}
