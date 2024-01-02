package commands.userCommands.normalUserCommands.playlistCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public final class SwitchVisibilityCommand extends NormalUserCommand {
    private final int playlistId;

    public SwitchVisibilityCommand(final CommandInput commandInput) {
        super(commandInput);
        playlistId = commandInput.getPlaylistId();
    }

    /**
     * Executes switch visibility command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("switchVisibility",
                                                                        output);
        if (objectNode == null) {
            return;
        }

        if (playlistId > normalUser.getPlaylists().size()) {
            objectNode.put("message", "The specified playlist ID is too high.");
            output.add(objectNode);
            return;
        }

        if (normalUser.getPlaylists().get(playlistId - 1).resetIsPublic()) {
            objectNode.put("message",
                    "Visibility status updated successfully to public.");
        } else {
            objectNode.put("message",
                    "Visibility status updated successfully to private.");
        }

        output.add(objectNode);
    }
}
