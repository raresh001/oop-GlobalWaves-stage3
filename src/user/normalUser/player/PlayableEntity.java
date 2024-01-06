package user.normalUser.player;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.searchCommands.SelectCommand;
import lombok.Getter;
import user.normalUser.search.bar.SearchableEntity;

@Getter
public abstract class PlayableEntity implements SearchableEntity {
    protected final String name;
    protected int noWatchers;

    public PlayableEntity(final String name1) {
        name = name1;
    }

    public enum AudioType {
        SONG,
        PODCAST,
        PLAYLIST,
        ALBUM
    }

    /**
     * @return type of the playing file
     */
    public abstract AudioType getType();

    /**
     * @param player the player playing the current file
     * @return the name of the current track
     */
    public abstract String getTrackName(Player player);

    /**
     * @param player the player playing the current file
     * @return the remaining playing time of the current track
     */
    public abstract int getRemainingTime(Player player);

    /**
     * @param track - the track running on the player
     * @return - the duration of the current played track
     */
    public abstract int getCurrentTrackDuration(int track);

    /**
     * Accept get next track operation (visit)
     * @param player - the player that needs to be updated
     * @return - if the player was NOT reset
     */
    public abstract boolean acceptGetNextTrack(Player player);

    /**
     * Accept listening to the current track
     * @param player - the player that is playing this file
     */
    public abstract void acceptListen(Player player);

    /**
     * @param repeat the player repeat state (0, 1 or 2)
     * @return the interpretation of repeat according to the current file
     */
     public String repeatState(final int repeat) {
        if (repeat == 0) {
            return "No Repeat";
        }

        if (repeat == 1) {
            return "Repeat Once";
        }

        return "Repeat Infinite";
    }

    /**
     * Indicates if this file can be deleted
     * @return a boolean indicating if the file has any dependencies left
     * (any player watching it)
     */
    public boolean canBeDeleted() {
         return noWatchers == 0;
    }

    /**
     * Accept the selection of this file (visit)
     * @param selectCommand - the command that selects this entity
     * @param objectNode - the json containing the result of this command
     */
    @Override
    public final void acceptSelect(final SelectCommand selectCommand, final ObjectNode objectNode) {
         selectCommand.select(this, objectNode);
    }

    /**
     * Set the player as watching this file
     * @param player - the player watching this file
     */
    public void watch(final Player player) {
        noWatchers++;
    }

    /**
     * Set the player as finishing watching this file
     * @param player - the player who stopped watching this file
     */
    public void unwatch(final Player player) {
        noWatchers--;
    }

    /**
     * @return - if this file is an ad
     */
    public boolean isAd() {
        return false;
    }
}
