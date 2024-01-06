package commands.userCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioCollections.Podcast;
import audio.audioFiles.Episode;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import user.User;
import user.artist.Artist;
import user.host.Host;
import user.normalUser.NormalUser;

import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

record SortCell(String name, int value) implements Comparable<SortCell> {
    @Override
    public int compareTo(final SortCell o) {
        return o.value == value() ? name.compareTo(o.name) : o.value - value;
    }

    /**
     * @param map - a map containing pairs (names - number of listens)
     * @return - an array of these pairs sorted by number of listens (alphabetical order
     * if the values are equal)
     */
    public static ArrayList<SortCell> getTopList(final HashMap<String, Integer> map) {
        ArrayList<SortCell> top = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            top.add(new SortCell(entry.getKey(), entry.getValue()));
        }

        top.sort((o1, o2) -> o1.value == o2.value()
                        ? o1.name.compareTo(o2.name) : o2.value - o1.value);
        return top;
    }
}

public final class WrappedCommand extends UserCommand {
    private static final int STATISTICS_MAX_LEN = 5;

    public enum WrapResult {
        VALID_OUTPUT,
        INVALID_USER,
        INVALID_ARTIST,
        INVALID_HOST
    }

    public WrappedCommand(CommandInput commandInput) {
        super(commandInput);
    }

    /* Create an object node that contains information about the list */
    private ObjectNode getTopArrayNode(final ArrayList<SortCell> list) {
        int max = 0;
        list.sort(null);
        ObjectNode top = (new ObjectMapper()).createObjectNode();

        for (SortCell sortCell : list) {
            top.put(sortCell.name(), sortCell.value());
            if (++max == STATISTICS_MAX_LEN) {
                break;
            }
        }

        return top;
    }

    /* Create an object node that contains information about the map */
    private ObjectNode getTopArrayNode(final HashMap<String, Integer> map) {
        final int maxSize = 5;
        int max = 0;
        ObjectNode top = (new ObjectMapper()).createObjectNode();
        for (SortCell sortCell : SortCell.getTopList(map)) {
            top.put(sortCell.name(), sortCell.value());
            if (++max == maxSize) {
                break;
            }
        }

        return top;
    }

    /* Create an object node that contains information about the orders names from map */
    private ArrayNode getTopArrayWithoutValue(final HashMap<String, Integer> map) {
        final int maxSize = 5;
        int max = 0;
        ArrayNode top = (new ObjectMapper()).createArrayNode();
        for (SortCell sortCell : SortCell.getTopList(map)) {
            top.add(sortCell.name());
            if (++max == maxSize) {
                break;
            }
        }

        return top;
    }

    /**
     * Wrap a normal user
     * @param normalUser - the user that is wrapped
     * @param objectNode - contains the result of the statistics
     * @return - the success rate of this command
     */
    public WrapResult wrap(final NormalUser normalUser, final ObjectNode objectNode) {
        if (normalUser.getPlayer().getListenedSongs().isEmpty()
                && normalUser.getPlayer().getListenedEpisodes().isEmpty()) {
            return WrapResult.INVALID_USER;
        }

        objectNode.set("topArtists", getTopArrayNode(normalUser.getPlayer().getListenedArtists()));
        objectNode.set("topGenres", getTopArrayNode(normalUser.getPlayer().getListenedGenres()));
        objectNode.set("topSongs", getTopArrayNode(normalUser.getPlayer().getListenedSongs()));
        objectNode.set("topAlbums", getTopArrayNode(normalUser.getPlayer().getListenedAlbums()));
        objectNode.set("topEpisodes",
                    getTopArrayNode(normalUser.getPlayer().getListenedEpisodes()));

        return WrapResult.VALID_OUTPUT;
    }

    private static void wrapAlbum(final Album album,
                              final HashMap<String, Integer> listenersMap,
                              final HashMap<String, Integer> topAlbums,
                              final HashMap<String, Integer> topSongs) {
        int totalAlbumListeners = 0;

        for (Song song : album.getSongs()) {
            int totalSongListeners = 0;

            for (Map.Entry<String, Integer> entry : song.getListeners().entrySet()) {
                listenersMap.put(entry.getKey(),
                        listenersMap.getOrDefault(entry.getKey(), 0)
                                + entry.getValue());
                totalSongListeners += entry.getValue();
            }

            if (totalSongListeners != 0) {
                topSongs.put(song.getName(),
                        topSongs.getOrDefault(song.getName(), 0) + totalSongListeners);
                totalAlbumListeners += totalSongListeners;
            }
        }

        if (totalAlbumListeners > 0) {
            topAlbums.put(album.getName(),
                    topAlbums.getOrDefault(album.getName(), 0) + totalAlbumListeners);
        }
    }

    /**
     * Wrap an artist
     * @param artist - the user that is wrapped
     * @param objectNode - contains the result of the statistics
     * @return - the success rate of this command
     */
    public WrapResult wrap(final Artist artist, final ObjectNode objectNode) {
        HashMap<String, Integer> topAlbums = new HashMap<>();
        HashMap<String, Integer> topSongs = new HashMap<>();

        HashMap<String, Integer> listenersMap = new HashMap<>();

        for (Album album : artist.getRemovedAlbums()) {
            wrapAlbum(album, listenersMap, topAlbums, topSongs);
        }

        for (Album album : artist.getAlbums()) {
            wrapAlbum(album, listenersMap, topAlbums, topSongs);
        }

        if (topSongs.isEmpty()) {
            return WrapResult.INVALID_ARTIST;
        }

        objectNode.set("topAlbums", getTopArrayNode(topAlbums));
        objectNode.set("topSongs", getTopArrayNode(topSongs));
        objectNode.set("topFans", getTopArrayWithoutValue(listenersMap));
        objectNode.put("listeners", listenersMap.size());

        return WrapResult.VALID_OUTPUT;
    }

    /**
     * Wrap a host
     * @param host - the user that is wrapped
     * @param objectNode - contains the result of the statistics
     * @return - the success rate of this command
     */
    public WrapResult wrap(final Host host, final ObjectNode objectNode) {
        HashMap<String, Integer> listenersMap = new HashMap<>();
        ArrayList<SortCell> topEpisodes = new ArrayList<>();

        for (Podcast podcast : host.getPodcasts()) {
            for (Episode episode : podcast.getEpisodes()) {
                int episodeWatches = 0;
                for (Map.Entry<String, Integer> entry : episode.getListeners().entrySet()) {
                    episodeWatches += entry.getValue();
                    listenersMap.put(entry.getKey(), 1);
                }
                if (episodeWatches > 0) {
                    topEpisodes.add(new SortCell(episode.getName(), episodeWatches));
                }
            }
        }

        if (topEpisodes.isEmpty()) {
            return WrapResult.INVALID_HOST;
        }

        objectNode.set("topEpisodes", getTopArrayNode(topEpisodes));
        objectNode.put("listeners", listenersMap.size());
        return WrapResult.VALID_OUTPUT;
    }

    /**
     * Execute wrapped command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateCommandResult("wrapped",
                                                            username,
                                                            timestamp);

        User user = Admin.getUserByName(username);
        if (user == null) {
            objectNode.put("message", "No data to show for user " + username + ".");
        } else {
            // update all players
            Admin.updatePlayers(timestamp);

            ObjectNode resultNode = (new ObjectMapper()).createObjectNode();

            switch (user.acceptWrap(this, resultNode)) {
                case VALID_OUTPUT:
                    objectNode.set("result", resultNode);
                    break;
                case INVALID_USER:
                    objectNode.put("message",
                                "No data to show for user " + username + ".");
                    break;
                case INVALID_ARTIST:
                    objectNode.put("message",
                                "No data to show for artist " + username + ".");
                    break;
                default:
                    objectNode.put("message",
                                "No data to show for host " + username + ".");
            }
        }

        output.add(objectNode);
    }
}
