package commands.userCommands.normalUserCommands.playerCommands;

import audio.audioCollections.Podcast;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;
import user.normalUser.player.Position;

public final class ForwardCommand extends NormalUserCommand {
    public ForwardCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes forward command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("forward", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                    "Please load a source before attempting to forward.");
            output.add(objectNode);
            return;
        }

        if (player.getPlayingAudioFile().getType() != PlayableEntity.AudioType.PODCAST) {
            objectNode.put("message", "The loaded source is not a podcast.");
            output.add(objectNode);
            return;
        }

        objectNode.put("message", "Skipped forward successfully.");
        output.add(objectNode);
        Podcast podcast = (Podcast) player.getPlayingAudioFile();

        if (player.getPosition().getPositionInTrack() + Podcast.FORWARD_BACKWARD_TIME
                < podcast.getEpisodes().get(player.getPosition().getTrack()).getDuration()) {
            player.setPosition(new Position(player.getPosition().getTrack(),
                                player.getPosition().getPositionInTrack()
                                                + Podcast.FORWARD_BACKWARD_TIME));

            return;
        }

        if (player.getPosition().getTrack() == podcast.getEpisodes().size() - 1) {
            switch (player.getRepeat()) {
                case 0:
                    player.reset();
                    return;
                case 1:
                    player.resetRepeat();
                    // Intentionally missed return
                default:
                    player.setPosition(new Position(0, 0));
                    return;
            }
        }

        player.setPosition(new Position(player.getPosition().getTrack() + 1,
                0));
    }
}
