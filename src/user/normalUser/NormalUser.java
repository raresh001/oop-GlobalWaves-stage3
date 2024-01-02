package user.normalUser;

import admin.Admin;
import audio.audioCollections.Playlist;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.WrappedCommand;
import fileio.input.UserInput;
import lombok.Getter;
import lombok.Setter;
import user.User;
import user.normalUser.page.HomePage;
import user.normalUser.page.Page;
import user.normalUser.player.Player;
import user.normalUser.search.bar.SearchBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

@Getter
public final class NormalUser extends User implements Observer {
    private final static double PREMIUM_USER_CREDIT = 1000000;

    private final LinkedList<Playlist> playlists = new LinkedList<>();
    private final LinkedList<Playlist> followedPlaylists = new LinkedList<>();
    private final LinkedList<Song> likedSongs = new LinkedList<>();
    private final SearchBar searchBar = new SearchBar();
    private final Player player;
    private final ArrayList<String> merchNameList = new ArrayList<>();

    @Setter
    private ArrayList<Notification> notifications = new ArrayList<>();
    @Setter
    private boolean isOnline = true;
    @Setter
    private Page page = new HomePage(this);

    public NormalUser(final UserInput userInput) {
        super(userInput);
        player = new Player(getName());
    }

    public NormalUser(final String name1, final int age1, final String city1) {
        super(name1, age1, city1);
        player = new Player(name1);
    }

    /**
     * @param timestamp - the moment when this player was requested
     * @return - the player, updated to the current moment of time (as
     * the application requires, the player remains unchanged if the user
     * was offline).
     */
    public Player getPlayer(final int timestamp) {
        if (isOnline()) {
            player.updatePosition(timestamp);
        }
        return player;
    }

    @Override
    public void acceptDelete() {
        Admin.removeUser(this);
    }

    @Override
    public boolean canBeDeleted() {
        if (player.isPremium()) {
            return false;
        }

        for (Playlist playlist : playlists) {
            if (!playlist.canBeDeleted()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public WrappedCommand.wrapResult acceptWrap(final WrappedCommand wrappedCommand, final ObjectNode objectNode) {
        return wrappedCommand.wrap(this, objectNode);
    }

    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }

    public void cancelPremium() {
        player.setPremium(false);

        for (Map.Entry<Song, Integer> entry : player.getWatchedSongs().entrySet()) {
            entry.getKey().addRevenue(PREMIUM_USER_CREDIT
                    * entry.getValue()
                    / player.getTotalNoWatchedSongs());
        }

        player.setWatchedSongs(new HashMap<>());
        player.setTotalNoWatchedSongs(0);
    }
}
