package commands.userCommands.normalUserCommands.playlistCommands;

import admin.Admin;
import audio.audioCollections.Playlist;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public final class CreatePlaylistCommand extends NormalUserCommand {
    private final String playlistName;

    public CreatePlaylistCommand(final CommandInput commandInput) {
        super(commandInput);
        playlistName = commandInput.getPlaylistName();
    }

    /**
     * Executes create playlist command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("createPlaylist", output);
        if (objectNode == null) {
            return;
        }

        for (Playlist playlist : normalUser.getPlaylists()) {
            if (playlist.getName().equals(playlistName)) {
                objectNode.put("message",
                        "A playlist with the same name already exists.");
                output.add(objectNode);
                return;
            }
        }

        Playlist newPlaylist = new Playlist(playlistName, username);
        normalUser.getPlaylists().add(newPlaylist);
        Admin.getPlaylists().add(newPlaylist);
        objectNode.put("message", "Playlist created successfully.");
        output.add(objectNode);
    }
}
