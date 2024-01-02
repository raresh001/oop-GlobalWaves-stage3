package commands.adminCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import user.artist.Artist;

public final class ShowAlbumsCommand extends Command {
    private final String user;
    public ShowAlbumsCommand(final CommandInput commandInput) {
        super(commandInput);
        user = commandInput.getUsername();
    }

    /**
     * Executes show albums command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = Command.createTemplateCommandResult("showAlbums",
                                                                        user,
                                                                        timestamp);
        Artist artist = Admin.getArtistByName(user);
        if (artist == null) {
            objectNode.put("message", user + " doesn't exist or is not an artist.");
            output.add(objectNode);
            return;
        }

        ArrayNode albumsArray = (new ObjectMapper()).createArrayNode();
        for (Album album : artist.getAlbums()) {
            ObjectNode albumObjectNode = (new ObjectMapper()).createObjectNode();
            albumObjectNode.put("name", album.getName());

            ArrayNode songsArray = (new ObjectMapper()).createArrayNode();
            for (Song song : album.getSongs()) {
                songsArray.add(song.getName());
            }

            albumObjectNode.set("songs", songsArray);
            albumsArray.add(albumObjectNode);
        }

        objectNode.set("result", albumsArray);
        output.add(objectNode);
    }
}
