package commands.userCommands.artistCommands;

import admin.Admin;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.userCommands.UserCommand;
import fileio.input.CommandInput;
import user.artist.Artist;

public abstract class ArtistCommand extends UserCommand {
    protected final Artist artist;
    protected ArtistCommand(final CommandInput commandInput) {
        super(commandInput);
        artist = Admin.getArtistByName(username);
    }

    protected final ObjectNode createTemplateArtistResult(final String command,
                                                          final ArrayNode output) {
        ObjectNode objectNode = Command.createTemplateCommandResult(command,
                                    username,
                                    timestamp);

        if (artist != null) {
            return objectNode;
        }

        if (Admin.getNormalUserByName(username) != null || Admin.getHostByName(username) != null) {
            objectNode.put("message", username + " is not an artist.");
            output.add(objectNode);
            return null;
        }

        objectNode.put("message", "The username " + username + " doesn't exist.");
        output.add(objectNode);
        return null;
    }
}
