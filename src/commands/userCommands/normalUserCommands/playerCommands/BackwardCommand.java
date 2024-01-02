package commands.userCommands.normalUserCommands.playerCommands;

import audio.audioCollections.Podcast;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;
import user.normalUser.player.Position;

public final class BackwardCommand extends NormalUserCommand {
    public BackwardCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes backward command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("backward", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message", "Please load a source before rewinding.");
            output.add(objectNode);
            return;
        }

        if (player.getPlayingAudioFile().getType() != PlayableEntity.AudioType.PODCAST) {
            objectNode.put("message", "The loaded source is not a podcast.");
            output.add(objectNode);
            return;
        }

        if (player.getPosition().getPositionInTrack() < Podcast.FORWARD_BACKWARD_TIME) {
            player.setPosition(new Position(player.getPosition().getTrack(), 0));
            return;
        }

        player.setPosition(new Position(player.getPosition().getTrack(),
                player.getPosition().getPositionInTrack()
                        - Podcast.FORWARD_BACKWARD_TIME));

        objectNode.put("message", "Rewound successfully.");
        output.add(objectNode);
    }
}
