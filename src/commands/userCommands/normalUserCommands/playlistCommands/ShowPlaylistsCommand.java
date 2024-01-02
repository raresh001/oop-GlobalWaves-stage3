package commands.userCommands.normalUserCommands.playlistCommands;

import audio.audioCollections.Playlist;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public final class ShowPlaylistsCommand extends NormalUserCommand {
    public ShowPlaylistsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes show playlists command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("showPlaylists", output);
        if (objectNode == null) {
            return;
        }

        final ObjectMapper objectMapper = new ObjectMapper();
        final ArrayNode resultArray = objectMapper.createArrayNode();
        for (Playlist playlist : normalUser.getPlaylists()) {
            final ObjectNode resultNode = objectMapper.createObjectNode();
            resultNode.put("name", playlist.getName());

            // Add songs
            final ArrayNode songsArray = objectMapper.createArrayNode();
            for (Song song : playlist.getSongs()) {
                songsArray.add(song.getName());
            }
            resultNode.set("songs", songsArray);

            if (playlist.isPublic()) {
                resultNode.put("visibility", "public");
            } else {
                resultNode.put("visibility", "private");
            }

            resultNode.put("followers", playlist.getNoFollowers());

            resultArray.add(resultNode);
        }

        objectNode.set("result", resultArray);
        output.add(objectNode);
    }
}
