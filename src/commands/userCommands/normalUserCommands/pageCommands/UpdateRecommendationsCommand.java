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

import java.util.*;

public final class UpdateRecommendationsCommand extends NormalUserCommand {
    private final String recommendationType;
    public UpdateRecommendationsCommand(CommandInput commandInput) {
        super(commandInput);
        recommendationType = commandInput.getRecommendationType();
    }

    private LinkedList<Song> getGenreSongs(String genre, Iterable<Song> songs) {
        LinkedList<Song> genreSongs = new LinkedList<>();
        for (Song song : songs) {
            if (song.getGenre().equalsIgnoreCase(genre)) {
                System.out.println("Am adaugat melodia " + song.getName());
                genreSongs.add(song);
            }
        }

        System.out.println(genreSongs.size());
        return genreSongs;
    }

    private void recommendRandomSong(int seed, String genre) {
        List<Song> genreSongs = getGenreSongs(genre, Admin.getSongs());
        if (!genreSongs.isEmpty()) {
            Song recommendation = genreSongs.get(new Random(seed).nextInt(genreSongs.size()));
            normalUser.setLastRecommendation(recommendation);
            normalUser.getRecommendedSongs()
                    .add(recommendation);
        }
    }

    private boolean recommendRandomSong(ObjectNode objectNode, ArrayNode output) {
        Player player = normalUser.getPlayer();
        if (player.getPlayingAudioFile() == null ||
                player.getPlayingAudioFile().getType() != PlayableEntity.AudioType.SONG) {
            objectNode.put("message", username + " is not playing a song.");
            output.add(objectNode);
            return false;
        }

        if (player.getPosition().getPositionInTrack() < 30) {
            objectNode.put("message",
                    username + "needs to watch at least 30s of this song.");
            output.add(objectNode);
            return false;
        }

        recommendRandomSong(player.getPosition().getPositionInTrack(),
                ((Song) player.getPlayingAudioFile()).getGenre());
        return true;
    }

    private Set<Song> getAllSongsForRandomPlaylist() {
        Set<Song> allSongs = new LinkedHashSet<>(normalUser.getLikedSongs());
        for (Playlist playlist : normalUser.getPlaylists()) {
            allSongs.addAll(playlist.getSongs());
        }

        for (Playlist playlist : normalUser.getFollowedPlaylists()) {
            allSongs.addAll(playlist.getSongs());
        }

        return allSongs;
    }

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

    private void recommendRandomPlaylist() {
        int[] topCount = {5, 3, 2};

        Set<Song> resultSongs = new LinkedHashSet<>();

        Set<Song> allSongs = getAllSongsForRandomPlaylist();
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

        Playlist playlist = new Playlist(username + "'s recommendations", username);
        playlist.getSongs().addAll(resultSongs);

        normalUser.getRecommendedPlaylists().add(playlist);
        normalUser.setLastRecommendation(playlist);
    }

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

    private static void getTopListenersArtist(final String name) {
        Artist artist = Admin.getArtistByName(name);

        HashMap<String, Integer> bestFans = new HashMap<>();
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                for (Map.Entry<String, Integer> songFans : song.getListeners().entrySet()) {
                    
                }
            }
        }
    }

    private Playlist getFansPlaylist(Song song) {
        ArrayList<String> topListeners = getTopListenersSong(song);
        ArrayList<NormalUser> topFans = new ArrayList<>();
        for (String username : topListeners) {
            topFans.add(Admin.getNormalUserByName(username));
        }



        return null;
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("updateRecommendations",
                                                                    output);
        if (objectNode == null) {
            return;
        }

        normalUser.getPlayer().updatePosition(timestamp);
        switch (recommendationType) {
            case "random_song":
                if (!recommendRandomSong(objectNode, output)) {
                    return;
                }
                break;
            case "random_playlist":
                recommendRandomPlaylist();
                break;
            case "fans_playlist":
                // TODO
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
