package commands.userCommands.normalUserCommands.playerCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;

public final class RepeatCommand extends NormalUserCommand {
    public RepeatCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes repeat command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("repeat", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                    "Please load a source before setting the repeat status.");
            output.add(objectNode);
            return;
        }

        player.setRepeat((player.getRepeat() + 1) % Player.NO_REPEAT_STATES);
        objectNode.put("message",
                    "Repeat mode changed to "
                        + player.getPlayingAudioFile().repeatState(player.getRepeat()).toLowerCase()
                        + ".");

        output.add(objectNode);
    }
}
