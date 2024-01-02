package user.normalUser.player;

import admin.Admin;
import audio.audioCollections.Podcast;
import audio.audioCollections.SongsCollection;
import audio.audioFiles.Episode;
import audio.audioFiles.Song;
import lombok.Getter;
import lombok.Setter;


import java.util.*;

@Getter
public final class Player {
    public static final int NO_REPEAT_STATES = 3;

    private final String username;

    @Setter
    private boolean isPremium;

    @Setter
    private double addPrice = -1;

    @Setter
    private PlayableEntity playingAudioFile;
    @Setter
    private Position position;

    private PlayableEntity prevPlayingFile;
    private Position prevPosition;

    @Setter
    private boolean isPlaying;
    @Setter
    private int repeat;
    @Setter
    private int timestamp;
    @Setter
    private ArrayList<Integer> shuffle;
    private final HashMap<String, Position> previousPodcastPositions = new HashMap<>();

    private final HashMap<String, Integer> listenedSongs = new HashMap<>();
    private final HashMap<String, Integer> listenedAlbums = new HashMap<>();
    private final HashMap<String, Integer> listenedGenres = new HashMap<>();
    private final HashMap<String, Integer> listenedArtists = new HashMap<>();
    private final HashMap<String, Integer> listenedEpisodes = new HashMap<>();

    @Setter
    private HashMap<Song, Integer> watchedSongs = new HashMap<>();
    @Setter
    private int totalNoWatchedSongs;

    public Player(final String username1) {
        username = username1;
    }

    /**
     * set repeat state to 0
     */
    public void resetRepeat() {
        repeat = 0;
    }

    public boolean isShuffled() {
        return shuffle != null;
    }

    public void listen(Song song, int value) {
        watchedSongs.put(song, watchedSongs.getOrDefault(song, 0) + value);
        totalNoWatchedSongs += value;

        listenedSongs.put(song.getName(), listenedSongs.getOrDefault(song.getName(), 0) + value);
        listenedAlbums.put(song.getAlbum(), listenedAlbums.getOrDefault(song.getAlbum(), 0) + value);
        listenedGenres.put(song.getGenre(), listenedGenres.getOrDefault(song.getGenre(), 0) + value);
        listenedArtists.put(song.getArtist(), listenedArtists.getOrDefault(song.getArtist(), 0) + value);
    }

    public void listen(Song song) {
        // System.out.println("Accept listen for " + song.getName());
        watchedSongs.put(song, watchedSongs.getOrDefault(song, 0) + 1);
        totalNoWatchedSongs++;

        listenedSongs.put(song.getName(), listenedSongs.getOrDefault(song.getName(), 0) + 1);
        listenedAlbums.put(song.getAlbum(), listenedAlbums.getOrDefault(song.getAlbum(), 0) + 1);
        listenedGenres.put(song.getGenre(), listenedGenres.getOrDefault(song.getGenre(), 0) + 1);
        listenedArtists.put(song.getArtist(), listenedArtists.getOrDefault(song.getArtist(), 0) + 1);
    }

    public void listen(Podcast podcast) {
        Episode episode = podcast.getEpisodes().get(position.getTrack());
        listenedEpisodes.put(episode.getName(), listenedEpisodes.getOrDefault(episode.getName(), 0) + 1);
    }

    public void listen(Episode episode, int value) {
        listenedEpisodes.put(episode.getName(), listenedEpisodes.getOrDefault(episode.getName(), 0) + value);
    }

    /**
     * reset the player (the playing file, shuffle options, playing options,
     * repeat options and the position)
     */
    public void reset() {
        if (playingAudioFile != null) {
            playingAudioFile.unwatch(this);
        }

        shuffle = null;
        repeat = 0;
        playingAudioFile = null;
        position = null;
        isPlaying = false;
    }

    public boolean getNextTrackSong() {
        switch (repeat) {
            case 0:
                reset();
                return false;
            case 1:
                repeat = 0;
            default:
                position = new Position(0, 0);
                return true;
        }
    }

    public boolean getNextTrackPodcast() {
        int track = position.getTrack();
        Podcast podcast = (Podcast) playingAudioFile;
        if (track == podcast.getEpisodes().size() - 1) {
            switch (repeat) {
                case 0:
                    previousPodcastPositions.remove(podcast.getName());
                    reset();
                    return false;
                case 1:
                    repeat = 0;
                default:
                    track = -1;
            }
        }

        position = new Position(1 + track, 0);
        return true;
    }

    public boolean getNextTrackSongsCollection() {
        if (repeat == 2) {
            // repeat current song
            return true;
        }

        int track = position.getTrack();
        List<Song> songs = ((SongsCollection) playingAudioFile).getSongs();

        if (shuffle != null) {
            int posInShuffle = shuffle.indexOf(track);
            if (posInShuffle == songs.size() - 1) {
                if (repeat == 0) {
                    reset();
                    return false;
                }

                repeat = 0;
                posInShuffle = -1;
            }
            track = shuffle.get(1 + posInShuffle);
        } else {
            track++;
            if (track == songs.size()) {
                if (repeat == 0) {
                    reset();
                    return false;
                }

                repeat = 0;
                track = 0;
            }
        }

        position = new Position(track, 0);
        return true;
    }

    public void playAd(final int timestamp1) {
        System.out.println("Am intrat aici " + addPrice);
        int posInTrack = position.getPositionInTrack();

        int duration = playingAudioFile.getCurrentTrackDuration(position.getTrack());
        playingAudioFile.acceptGetNextTrack(this);

        timestamp += (duration - posInTrack);

        prevPlayingFile = playingAudioFile;
        prevPosition = position;

        position = new Position(0, 0);
        playingAudioFile = Admin.getAdd();

        System.out.println("Am iesit de aici " + addPrice);

        updatePosition(timestamp1);
    }

    private void playBackNormal(final int timestamp1) {
        calculateCreditAd();

        addPrice = -1;
        timestamp += (playingAudioFile.getCurrentTrackDuration(0) - position.getPositionInTrack());

        playingAudioFile = prevPlayingFile;
        position = prevPosition;

        if (playingAudioFile != null) {
            playingAudioFile.acceptListen(this);
        }

        updatePosition(timestamp1);
    }

    public void updatePosition(final int timestamp1) {
        if (playingAudioFile == null) {
            timestamp = timestamp1;
            return;
        }

        int timestampDiff = timestamp1 - timestamp + position.getPositionInTrack();

        if (playingAudioFile.isAd()) {
            System.out.println("AICI: " + addPrice);
            if (timestampDiff >= playingAudioFile.getCurrentTrackDuration(position.getTrack())) {
                playBackNormal(timestamp1);
                return;
            }
            position.setPositionInTrack(timestampDiff);
            timestamp = timestamp1;
            return;
        }

        if (addPrice != -1 &&
                timestampDiff >= playingAudioFile.getCurrentTrackDuration(position.getTrack())) {
            playAd(timestamp1);
            return;
        }

        // System.out.println("AM ajuns si aici");
        while (timestampDiff >= playingAudioFile.getCurrentTrackDuration(position.getTrack())) {
            timestampDiff -= playingAudioFile.getCurrentTrackDuration(position.getTrack());

            if (!playingAudioFile.acceptGetNextTrack(this)) {
                timestamp = timestamp1;
                return;
            }

           //  System.out.println("IN acelasi album");
            playingAudioFile.acceptListen(this);
        }

        position.setPositionInTrack(timestampDiff);
        timestamp = timestamp1;
    }

//    private void updateSong(final int timestamp1) {
//        int timestampDiff = timestamp1 - timestamp;
//        Song playedSong = (Song) playingAudioFile;
//
//        if (playedSong.getDuration() > position.getPositionInTrack() + timestampDiff) {
//            position = new Position(0,
//                    position.getPositionInTrack() + timestampDiff);
//            return;
//        }
//
//        if (repeat == 2) {
//            playedSong.acceptListen((position.getPositionInTrack() + timestampDiff)
//                                        / playedSong.getDuration(),
//                                        this);
//
//            position = new Position(0,
//                    (position.getPositionInTrack() + timestampDiff)
//                            % playedSong.getDuration());
//
//            return;
//        }
//
//        if ((2 * playedSong.getDuration() > position.getPositionInTrack() + timestampDiff)
//            && (repeat == 1)) {
//            repeat = 0;
//
//            position = new Position(0,
//                    position.getPositionInTrack()
//                            + timestampDiff
//                            - playedSong.getDuration());
//
//            playedSong.acceptListen(1, this);
//            return;
//        }
//
//        reset();
//    }
//
//    private void updateSongsCollectionIfRepeatSong(final int timestampDiff) {
//        List<Song> songs = ((SongsCollection) playingAudioFile).getSongs();
//        songs.get(position.getTrack())
//                .acceptListen((position.getPositionInTrack() + timestampDiff)
//                                / songs.get(position.getTrack()).getDuration(),
//                        this);
//
//        position = new Position(position.getTrack(),
//                (position.getPositionInTrack() + timestampDiff)
//                        % songs.get(position.getTrack()).getDuration());
//
//    }
//
//    private void updateSongsCollection(final int timestamp1) {
//        int timestampDiff = timestamp1 - timestamp;
//        List<Song> songs = ((SongsCollection) playingAudioFile).getSongs();
//
//        if (repeat == 2) {
//            updateSongsCollectionIfRepeatSong(timestampDiff);
//        }
//
//        int track = position.getTrack();
//
//        // reset the playing difference as if the current track has just begun
//        timestampDiff += position.getPositionInTrack();
//
//        while (timestampDiff >= songs.get(track).getDuration()) {
//            timestampDiff -= songs.get(track).getDuration();
//
//            if (shuffle != null) {
//                int posInShuffle = shuffle.indexOf(track);
//                if (posInShuffle == songs.size() - 1) {
//                    if (repeat == 0) {
//                        reset();
//                        return;
//                    }
//
//                    posInShuffle = -1;
//                }
//                track = shuffle.get(1 + posInShuffle);
//            } else {
//                track++;
//                if (track == songs.size()) {
//                    if (repeat == 0) {
//                        reset();
//                        return;
//                    }
//
//                    track = 0;
//                }
//            }
//
//            songs.get(track).acceptListen(1, this);
//        }
//        position = new Position(track, timestampDiff);
//    }
//
//    private void updatePodcast(final int timestamp1) {
//        int timestampDiff = timestamp1 - timestamp;
//        int track = position.getTrack();
//        Podcast podcast = (Podcast) playingAudioFile;
//
//        // reset the playing difference as if the current track has just begun
//        timestampDiff += position.getPositionInTrack();
//
//        while (timestampDiff >= podcast.getEpisodes().get(track).getDuration()) {
//            timestampDiff -= podcast.getEpisodes().get(track).getDuration();
//            track++;
//            if (track == podcast.getEpisodes().size()) {
//                switch (repeat) {
//                    case 0:
//                        previousPodcastPositions.remove(podcast.getName());
//                        reset();
//                        return;
//                    case 1:
//                        repeat = 0;
//                        // Intentionally missed break
//                    default:
//                        track = 0;
//                }
//            }
//
//            podcast.getEpisodes().get(track).acceptListen(1, this);
//        }
//
//        position = new Position(track, timestampDiff);
//    }
//
//    /**
//     * update the playing audio file to the new moment
//     * @param timestamp1 - the current timestamp
//     */
//    public void updatePosition2(final int timestamp1) {
//        if (playingAudioFile == null || !isPlaying) {
//            timestamp = timestamp1;
//            return;
//        }
//
//        switch (playingAudioFile.getType()) {
//            case SONG:
//                updateSong(timestamp1);
//                break;
//            case PLAYLIST:
//            case ALBUM:
//                updateSongsCollection(timestamp1);
//                break;
//            default: // Podcast
//                updatePodcast(timestamp1);
//        }
//
//        timestamp = timestamp1;
//    }

    /**
     * retain its current position (if the current file is a podcast)
     */
    public void retainPreviousPosition() {
        if (playingAudioFile == null
                || playingAudioFile.getType() != PlayableEntity.AudioType.PODCAST) {
            // for podcasts only
            return;
        }

        if (previousPodcastPositions.containsKey(playingAudioFile.getName())) {
            previousPodcastPositions.replace(playingAudioFile.getName(), position);
        } else {
            previousPodcastPositions.put(playingAudioFile.getName(), position);
        }
    }

    /**
     * @return - remaining time of the playing file or 0 (if the playing file is null)
     */
    public int getRemainingTime() {
        if (playingAudioFile == null) {
            return 0;
        }

        return playingAudioFile.getRemainingTime(this);
    }

    /**
     * @return - current track of the playing file or "" (if the playing file is null)
     */
    public String getTrackName() {
        if (playingAudioFile == null) {
            return "";
        }

        return playingAudioFile.getTrackName(this);
    }

    /**
     * @return - repeat string of the playing file or "No Repeat" (if the playing file is null)
     */
    public String getRepeatString() {
        if (playingAudioFile == null) {
            return "No Repeat";
        }

        return playingAudioFile.repeatState(repeat);
    }

    /**
     * @return - shuffle status of the playing file or false (if the playing file is null)
     */
    public boolean getShuffledStatus() {
        if (playingAudioFile == null) {
            return false;
        }

        return shuffle != null;
    }

    /**
     * @return - paused status of the playing file or true (if the playing file is null)
     */
    public boolean getPausedStatus() {
        if (playingAudioFile == null) {
            return true;
        }

        return !isPlaying;
    }

    public void addBreak(double price) {
        addPrice = price;
        System.out.println("Schimb la " + price);
    }

    public void calculateCreditAd() {
        for (Map.Entry<Song, Integer> entry : watchedSongs.entrySet()) {
            System.out.println("DAM " + addPrice * entry.getValue() / totalNoWatchedSongs + " pt " + entry.getKey().getName());
            entry.getKey().addRevenue(addPrice * entry.getValue() / totalNoWatchedSongs);
        }

        System.out.println("GATA CU DATUL\n");
        totalNoWatchedSongs = 0;
        watchedSongs = new HashMap<>();
    }
}
