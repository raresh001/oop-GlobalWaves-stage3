package commands.userCommands.normalUserCommands.playerCommands;

import audio.audioCollections.Album;
import audio.audioCollections.SongsCollection;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;
import user.normalUser.player.Position;
import user.normalUser.search.bar.SearchBar;

public final class LoadCommand extends NormalUserCommand {
    public LoadCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes load command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("load", output);
        if (objectNode == null) {
            return;
        }

        SearchBar searchBar = normalUser.getSearchBar();

        if (!searchBar.isSelected()) {
            objectNode.put("message",
                        "Please select a source before attempting to load.");
            output.add(objectNode);
            return;
        }

        if (searchBar.getSelectedEntity() == null) {
            objectNode.put("message", "You can't load an empty audio collection!");
            searchBar.setSelected(false);
            output.add(objectNode);
            return;
        }

        // effectuate the load
        Player player = normalUser.getPlayer(timestamp);

        // Retain previous position (for podcasts)
        player.retainPreviousPosition();

        // set current playing file as not watched
        if (player.getPlayingAudioFile() != null) {
            player.getPlayingAudioFile().unwatch(player);
        }

        player.setPlayingAudioFile(searchBar.getSelectedEntity());
        player.getPlayingAudioFile().watch(player);
        if (player.getPlayingAudioFile().getType() == PlayableEntity.AudioType.PODCAST) {
            player.setPosition(player.getPreviousPodcastPositions()
                    .getOrDefault(searchBar.getSelectedEntity().getName(),
                            new Position(0, 0)));

        } else {
            player.setPosition(new Position(0, 0));
        }

        switch (player.getPlayingAudioFile().getType()) {
            case SONG:
                ((Song) player.getPlayingAudioFile()).acceptListen(1, player);
                break;
            case ALBUM:
            case PLAYLIST:
                ((SongsCollection) player.getPlayingAudioFile()).getSongs()
                        .get(player.getPosition().getTrack()).acceptListen(1, player);
        }

        player.setPlaying(true);
        player.setShuffle(null);
        player.resetRepeat();

        // player's timestamp must be reset in spite of the retainPreviousPosition
        // for cases when it wasn't previously playing anything
        player.setTimestamp(timestamp);

        // reset the search bar
        searchBar.reset();

        objectNode.put("message", "Playback loaded successfully.");

        output.add(objectNode);
    }
}
