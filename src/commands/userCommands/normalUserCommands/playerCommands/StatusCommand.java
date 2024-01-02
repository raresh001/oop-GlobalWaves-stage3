package commands.userCommands.normalUserCommands.playerCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;

public final class StatusCommand extends NormalUserCommand {
    public StatusCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes status command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("status", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        ObjectNode aux = (new ObjectMapper()).createObjectNode();
        aux.put("name", player.getTrackName());
        aux.put("remainedTime", player.getRemainingTime());
        aux.put("repeat", player.getRepeatString());
        aux.put("shuffle", player.getShuffledStatus());
        aux.put("paused", player.getPausedStatus());

        objectNode.set("stats", aux);
        output.add(objectNode);
    }
}
