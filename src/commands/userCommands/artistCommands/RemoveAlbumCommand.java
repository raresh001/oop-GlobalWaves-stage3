package commands.userCommands.artistCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class RemoveAlbumCommand extends ArtistCommand {
    private final String name;
    public RemoveAlbumCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    /**
     * Executes remove album command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateArtistResult("removeAlbum", output);
        if (objectNode == null) {
            return;
        }

        for (Album album : artist.getAlbums()) {
            if (!album.getName().equals(name)) {
                continue;
            }

            Admin.updatePlayers(timestamp);

            if (!album.canBeDeleted()) {
                objectNode.put("message", username + " can't delete this album.");
                output.add(objectNode);
                return;
            }

            artist.getAlbums().remove(album);
            for (Song song : album.getSongs()) {
                Admin.getSongs().remove(song);
            }
            objectNode.put("message", username + " deleted the album successfully.");
            output.add(objectNode);
            return;
        }

        objectNode.put("message", username
                                            + " doesn't have an album with the given name.");
        output.add(objectNode);
    }
}
