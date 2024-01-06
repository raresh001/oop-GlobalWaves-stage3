package user.artist;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.WrappedCommand;
import commands.userCommands.normalUserCommands.searchCommands.SelectCommand;
import lombok.Getter;
import user.User;
import user.normalUser.NormalUser;
import user.normalUser.Notification;
import user.normalUser.Subject;
import user.normalUser.search.bar.SearchableEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class Artist extends User implements SearchableEntity {
    private final ArrayList<Album> removedAlbums = new ArrayList<>();
    private final List<Album> albums = new LinkedList<>();
    private final LinkedList<Event> events = new LinkedList<>();
    private final LinkedList<Merch> merchList = new LinkedList<>();

    private final Subject subject = new Subject();

    private double merchRevenue;

    public Artist(final String name1, final int age1, final String city1) {
        super(name1, age1, city1);
    }

    /**
     * @return - total revenues from all songs of the artist (including removed ones)
     */
    public double getTotalSongRevenue() {
        double totalRevenue = 0;
        for (Album album : removedAlbums) {
            for (Song song : album.getSongs()) {
                totalRevenue += song.getRevenues();
            }
        }

        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                totalRevenue += song.getRevenues();
            }
        }

        return totalRevenue;
    }

    public double getTotalRevenue() {
        return merchRevenue + getTotalSongRevenue();
    }

    @Override
    public void acceptDelete() {
        Admin.removeUser(this);
    }

    @Override
    public boolean canBeDeleted() {
        for (NormalUser normalUser : Admin.getNormalUsers()) {
            if (normalUser.getPage().isCreatedByUser(getName())) {
                return false;
            }
        }

        for (Album album : albums) {
            if (!album.canBeDeleted()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void acceptSelect(final SelectCommand selectCommand, final ObjectNode objectNode) {
        selectCommand.select(this, objectNode);
    }

    /**
     * Attempts to buy a merch of the current artist
     * @param name - the name of the merch
     * @return - indicates if the operation was successful (if the artist owns that merch)
     */
    public boolean buyMerch(final String name) {
        for (Merch merch : merchList) {
            if (merch.getName().equals(name)) {
                merchRevenue += merch.getPrice();
                return true;
            }
        }

        return false;
    }

    @Override
    public WrappedCommand.WrapResult acceptWrap(final WrappedCommand wrappedCommand,
                                                final ObjectNode objectNode) {
        return wrappedCommand.wrap(this, objectNode);
    }

    /**
     * notify all observers
     * @param notification - the notification that needs to be sent to all observers
     */
    public void notifySubscribers(final Notification notification) {
        subject.notify(notification);
    }

    /**
     * @return - if the artist has any merchandise bought or any listened song
     */
    public boolean hasNotPlaysOrMerch() {
        if (merchRevenue != 0.0) {
            return false;
        }

        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                if (!song.getListeners().isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    /* Get a hash map of all song prices (it adds the duplicate names) */
    private HashMap<String, Double> getSongPrices() {
        HashMap<String, Double> songPrices = new HashMap<>();

        for (Album album : removedAlbums) {
            for (Song song : album.getSongs()) {
                songPrices.put(song.getName(),
                                            songPrices.getOrDefault(song.getName(), 0.0)
                                                + song.getRevenues());
            }
        }

        for (Album album : albums) {
            for (Song song : album.getSongs()) {
                songPrices.put(song.getName(),
                                            songPrices.getOrDefault(song.getName(), 0.0)
                                                    + song.getRevenues());
            }
        }

        return songPrices;
    }

    private String getMostProfitableSong() {
        HashMap<String, Double> songPrices = getSongPrices();

        String best = null;
        double bestPrice = -1;

        for (Map.Entry<String, Double> entry : songPrices.entrySet()) {
            if (entry.getValue() > bestPrice) {
                best = entry.getKey();
                bestPrice = entry.getValue();
            } else if (bestPrice == entry.getValue() && best.compareTo(entry.getKey()) > 0) {
                best = entry.getKey();
                bestPrice = entry.getValue();
            }
        }

        return best;
    }

    /**
     * Show the statistics of the artist, in the way that the program ending requires
     * @param indexInArray - the index of the artist in Admin.getArtists()
     * @return - the object node containing the required information
     */
    public ObjectNode showStatistics(final int indexInArray) {
        final int oneHundred = 100;
        double songRevenue = (double) Math.round(getTotalSongRevenue() * oneHundred) / oneHundred;

        ObjectNode statisticsNode = (new ObjectMapper()).createObjectNode();
        statisticsNode.put("merchRevenue", merchRevenue);
        statisticsNode.put("songRevenue", songRevenue);
        statisticsNode.put("ranking", 1 + indexInArray);
        if (songRevenue != 0.0) {
            statisticsNode.put("mostProfitableSong", getMostProfitableSong());
        } else {
            statisticsNode.put("mostProfitableSong", "N/A");
        }

        return statisticsNode;
    }
}
