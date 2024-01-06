package commands.userCommands.normalUserCommands.pageCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioCollections.Playlist;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.artist.Artist;
import user.normalUser.NormalUser;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Set;
import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Comparator;

public final class UpdateRecommendationsCommand extends NormalUserCommand {
    private final String recommendationType;
    public UpdateRecommendationsCommand(final CommandInput commandInput) {
        super(commandInput);
        recommendationType = commandInput.getRecommendationType();
    }

    /* Get all songs from the given collection that have the specified genre */
    private LinkedList<Song> getGenreSongs(final String genre, final Iterable<Song> songs) {
        LinkedList<Song> genreSongs = new LinkedList<>();
        for (Song song : songs) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                genreSongs.add(song);
            }
        }

        return genreSongs;
    }

    /* Get the random song from the genre array, based on the given seed */
    private boolean recommendRandomSong(final int seed, final String genre) {
        List<Song> genreSongs = getGenreSongs(genre, Admin.getSongs());
        if (genreSongs.isEmpty()) {
            return false;
        }

        Song recommendation = genreSongs.get(new Random(seed).nextInt(genreSongs.size()));
        if (normalUser.getLastRecommendation() == recommendation) {
            return false;
        }
        normalUser.setLastRecommendation(recommendation);
        normalUser.getRecommendedSongs()
                .add(recommendation);

        return true;
    }

    /* Recommend a random song, if the current song has been played for at least 30s */
    private boolean recommendRandomSong(ObjectNode objectNode, ArrayNode output) {
        Player player = normalUser.getPlayer();

        if (player.getPosition().getPositionInTrack() < 30
                || !recommendRandomSong(player.getPosition().getPositionInTrack(),
                ((Song) player.getPlayingAudioFile()).getGenre())) {
            objectNode.put("message", "No new recommendations were found");
            output.add(objectNode);
            return false;
        }
        return true;
    }

    /* Get a set of songs that combines those from the followed and created playlists,
     *  as well as the liked songs
     */
    private Set<Song> getAllSongsForRandomPlaylist() {
        Set<Song> allSongs = new LinkedHashSet<>(normalUser.getLikedSongs());
        for (Playlist playlist : normalUser.getPlaylists()) {
            allSongs.addAll(playlist.getSongs());
        }

        for (Playlist playlist : normalUser.getPlaylists()) {
            allSongs.addAll(playlist.getSongs());
        }

        for (Playlist playlist : normalUser.getFollowedPlaylists()) {
            allSongs.addAll(playlist.getSongs());
        }

        return allSongs;
    }

    /* Get the top 3 genres from the analyzed songs */
    private static ArrayList<String> getTopGenres(final Set<Song> allSongs) {
        HashMap<String, Integer> genresHashMap = new HashMap<>();

        for (Song song : allSongs) {
            genresHashMap.put(song.getGenre(), genresHashMap.getOrDefault(song.getGenre(), 0) + 1);
        }

        ArrayList<String> topGenres = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            String bestGenre = null;
            int bestValue = -1;

            for (Map.Entry<String, Integer> entry : genresHashMap.entrySet()) {
                if (!topGenres.contains(entry.getKey()) && entry.getValue() > bestValue) {
                    bestGenre = entry.getKey();
                    bestValue = entry.getValue();
                }
            }

            if (bestGenre == null) {
                break;
            }

            topGenres.add(bestGenre);
        }

        return topGenres;
    }

    /* Recommend a random playlist, if there are any songs in the followed playlists, created
     * playlists or the liked array
     */
    private boolean recommendRandomPlaylist(final ObjectNode objectNode, final ArrayNode output) {
        int[] topCount = {5, 3, 2};

        Playlist playlist = new Playlist(username + "'s recommendations", username);
        Set<Song> resultSongs = new LinkedHashSet<>();
        Set<Song> allSongs = getAllSongsForRandomPlaylist();

        if (allSongs.isEmpty()) {
            objectNode.put("message", "No new recommendations were found");
            output.add(objectNode);
            return false;
        }

        ArrayList<String> topGenres = getTopGenres(allSongs);

        for (int i = 0; i < topGenres.size(); i++) {
            LinkedList<Song> genreSongs = getGenreSongs(topGenres.get(i), allSongs);
            genreSongs.sort(Comparator.comparingInt(song -> song.getLikes().size()));

            while (!genreSongs.isEmpty() && topCount[i] > 0) {
                if (resultSongs.add(genreSongs.removeLast())) {
                    topCount[i]--;
                }
            }
        }
        playlist.getSongs().addAll(resultSongs);

        normalUser.getRecommendedPlaylists().add(playlist);
        normalUser.setLastRecommendation(playlist);
        return true;
    }

    /* Get top 5 listeners of the given song */
    private static ArrayList<String> getTopListenersSong(Song song) {
        final int topFansLimit = 5;
        final ArrayList<String> topFans = new ArrayList<>(topFansLimit);

        for (int i = 0; i < topFansLimit; i++) {
            String bestFan = null;
            int bestListens = -1;

            for (Map.Entry<String, Integer> entry : song.getListeners().entrySet()) {
                if (!topFans.contains(entry.getKey()) && entry.getValue() > bestListens) {
                    bestFan = entry.getKey();
                    bestListens = entry.getValue();
                }
            }

            if (bestFan == null) {
                break;
            }
            topFans.add(bestFan);
        }

        return topFans;
    }

    /* Get top 5 listeners of the artist with the given name */
    private static ArrayList<String> getTopListenersArtist(final String name) {
        Artist artist = Admin.getArtistByName(name);

        HashMap<String, Integer> bestFansMap = new HashMap<>();
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                for (Map.Entry<String, Integer> songFans : song.getListeners().entrySet()) {
                    bestFansMap.put(songFans.getKey(),
                            bestFansMap.getOrDefault(songFans.getKey(), 0)
                                    + songFans.getValue());
                }
            }
        }

        final ArrayList<String> bestFans = new ArrayList<>();
        final int topFansLimit = 5;
        for (int i = 0; i < topFansLimit; i++) {
            String bestFan = null;
            int bestListens = -1;

            for (Map.Entry<String, Integer> fan : bestFansMap.entrySet()) {
                if (!bestFans.contains(fan.getKey()) && fan.getValue() > bestListens) {
                    bestFan = fan.getKey();
                    bestListens = fan.getValue();
                }

                if (bestFan == null) {
                    return bestFans;
                }

                bestFans.add(bestFan);
            }
        }

        return bestFans;
    }

    /* Concatenate the 2 arrays in the first one (considering that there are no duplicated) */
    private void getTotalFans(ArrayList<String> songFans, ArrayList<String> artistFans) {
        final int totalArtistFans = 5;
        int noElemsAdded = 0;
        for (String s : artistFans) {
            if (!songFans.contains(s)) {
                songFans.add(s);
                if (++noElemsAdded == totalArtistFans) {
                    return;
                }
            }
        }
    }

    /* Recommend fans playlist, if the user has liked any song */
    private boolean recommendFansPlaylist(ObjectNode objectNode, ArrayNode output) {
        if (normalUser.getLikedSongs().isEmpty()) {
            objectNode.put("message", "No new recommendations were found");
            output.add(objectNode);
            return false;
        }

        Song playedSong = (Song) normalUser.getPlayer().getPlayingAudioFile();

        Playlist playlist = new Playlist(playedSong.getArtist() + " Fan Club recommendations",
                                            username);
        ArrayList<String> fans = getTopListenersSong(playedSong);
        getTotalFans(fans, getTopListenersArtist(playedSong.getArtist()));

        List<Song> playlistSongs = playlist.getSongs();

        for (String fan : fans) {
            NormalUser fanUser = Admin.getNormalUserByName(fan);

            PriorityQueue<Song> priorityQueue
                    = new PriorityQueue<>(Comparator.comparingInt(song -> song.getLikes().size()));

            for (Song song : fanUser.getLikedSongs()) {
                if (song.getLikes().contains(normalUser) && !playlistSongs.contains(song)) {
                    priorityQueue.add(song);
                }
            }

            for (int i = 0; i < 5 && !priorityQueue.isEmpty(); i++) {
                playlistSongs.add(priorityQueue.remove());
            }
        }

        normalUser.getPlaylists().add(playlist);
        normalUser.setLastRecommendation(playlist);
        normalUser.getRecommendedPlaylists().add(playlist);
        return true;
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("updateRecommendations",
                                                                    output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null
                || player.getPlayingAudioFile().getType() != PlayableEntity.AudioType.SONG) {
            objectNode.put("message", "No new recommendations were found");
            output.add(objectNode);
            return;
        }

        switch (recommendationType) {
            case "random_song":
                if (!recommendRandomSong(objectNode, output)) {
                    return;
                }
                break;
            case "random_playlist":
                if (!recommendRandomPlaylist(objectNode, output)) {
                    return;
                }
                break;
            case "fans_playlist":
                if (!recommendFansPlaylist(objectNode, output)) {
                    return;
                }
                break;
            default:
                objectNode.put("message", recommendationType + " is not valid.");
                output.add(objectNode);
                return;
        }

        objectNode.put("message",
                "The recommendations for user " + username + " have been updated successfully.");
        output.add(objectNode);
    }
}
