package commands.userCommands.normalUserCommands.playerCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;

public final class PlayPauseCommand extends NormalUserCommand {
    public PlayPauseCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes play-pause command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("playPause", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                        "Please load a source before attempting to pause or resume playback.");
            output.add(objectNode);
            return;
        }

        player.setPlaying(!player.isPlaying());
        if (player.isPlaying()) {
            objectNode.put("message", "Playback resumed successfully.");
        } else {
            objectNode.put("message", "Playback paused successfully.");
        }

        output.add(objectNode);
    }
}
