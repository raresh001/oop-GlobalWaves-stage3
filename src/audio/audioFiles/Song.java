package audio.audioFiles;

import admin.Admin;
import audio.audioCollections.Playlist;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.SongInput;
import lombok.Getter;
import user.normalUser.NormalUser;
import user.normalUser.player.Listenable;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

@Getter
public final class Song extends PlayableEntity {
    private final int duration;
    private final String album;
    private final ArrayList<String> tags;
    private final String lyrics;
    private final String genre;
    private final int releaseYear;
    private final String artist;

    private double revenues;

    private final ArrayList<NormalUser> likes = new ArrayList<>();
    private final ArrayList<Playlist> playlistList = new ArrayList<>();
    private final HashMap<String, Integer> listeners = new HashMap<>();

    public Song(final SongInput songInput) {
        super(songInput.getName());
        this.duration = songInput.getDuration();
        this.album = songInput.getAlbum();
        this.tags = songInput.getTags();
        this.lyrics = songInput.getLyrics();
        this.genre = songInput.getGenre();
        this.releaseYear = songInput.getReleaseYear();
        this.artist = songInput.getArtist();
    }

    @Override
    public AudioType getType() {
        return AudioType.SONG;
    }

    @Override
    public String getTrackName(final Player player) {
        return name;
    }

    @Override
    public int getRemainingTime(final Player player) {
        return duration - player.getPosition().getPositionInTrack();
    }

    @Override
    public int getCurrentTrackDuration(int track) {
        return duration;
    }

    @Override
    public boolean acceptGetNextTrack(Player player) {
        return player.getNextTrackSong();
    }

//    @Override
//    public void acceptListen(Player player) {
//        String username = player.getUsername();
//        listeners.put(username, listeners.getOrDefault(username, 0) + 1);
//
//        player.listen(this, 1);
//    }

    /**
     * like or unlike this song
     * @param normalUser - the normal user that commanded like
     * @param objectNode - the json containing the result of this command
     */
    public void like(final NormalUser normalUser, final ObjectNode objectNode) {
        for (Song song : normalUser.getLikedSongs()) {
            if (song.equals(this)) {
                normalUser.getLikedSongs().remove(this);
                likes.remove(normalUser);
                objectNode.put("message", "Unlike registered successfully.");
                return;
            }
        }

        normalUser.getLikedSongs().add(this);
        likes.add(normalUser);
        objectNode.put("message", "Like registered successfully.");
    }

    @Override
    public void acceptListen(Player player) {
        String username = player.getUsername();
        listeners.put(username, listeners.getOrDefault(username, 0) + 1);

        player.listen(this);
    }

    public void addRevenue(double price) {
        revenues += price;
    }

    @Override
    public boolean isAd() {
        return name.equals(Admin.getAdd().getName());
    }
}
