package commands.userCommands.normalUserCommands.playlistCommands;

import audio.audioCollections.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.PlayableEntity;
import user.normalUser.search.bar.SearchBar;

public final class FollowPlaylistCommand extends NormalUserCommand {
    public FollowPlaylistCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes follow playlist command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("follow", output);
        if (objectNode == null) {
            return;
        }

        SearchBar searchBar = normalUser.getSearchBar();
        if (!searchBar.isSelected()) {
            objectNode.put("message",
                    "Please select a source before following or unfollowing.");
            output.add(objectNode);
            return;
        }

        if (searchBar.getSelectedEntity().getType() != PlayableEntity.AudioType.PLAYLIST) {
            objectNode.put("message", "The selected source is not a playlist.");
            output.add(objectNode);
            return;
        }

        ((Playlist) searchBar.getSelectedEntity()).followPlaylist(normalUser, objectNode);

        output.add(objectNode);
    }
}
