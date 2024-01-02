package commands.userCommands.normalUserCommands;

import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;

public final class ShowPreferredSongsCommand extends NormalUserCommand {
    public ShowPreferredSongsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode =
                Command.createTemplateCommandResult("showPreferredSongs",
                                                        username,
                                                        timestamp);

        final ArrayNode songArray = (new ObjectMapper()).createArrayNode();
        for (Song song : normalUser.getLikedSongs()) {
            songArray.add(song.getName());
        }

        objectNode.set("result", songArray);
        output.add(objectNode);
    }
}
