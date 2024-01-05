package commands.userCommands.normalUserCommands.playerCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;
import user.normalUser.player.Position;

public class LoadRecommendationsCommand extends NormalUserCommand {
    public LoadRecommendationsCommand(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("loadRecommendations",
                                                                    output);
        if (objectNode == null) {
            return;
        }

        if (normalUser.getLastRecommendation() == null) {
            objectNode.put("message", "No recommendations available.");
            output.add(objectNode);
            return;
        }

        // effectuate the load
        Player player = normalUser.getPlayer(timestamp);

        // Retain previous position (for podcasts)
        player.retainPreviousPosition();

        // set current playing file as not watched
        if (player.getPlayingAudioFile() != null) {
            player.getPlayingAudioFile().unwatch(player);
        }

        player.setPlayingAudioFile(normalUser.getLastRecommendation());
        normalUser.setLastRecommendation(null);
        player.getPlayingAudioFile().watch(player);

        player.setPosition(new Position(0, 0));

        player.getPlayingAudioFile().acceptListen(player);
        player.setPlaying(true);
        player.setShuffle(null);
        player.resetRepeat();

        // player's timestamp must be reset in spite of the retainPreviousPosition
        // for cases when it wasn't previously playing anything
        player.setTimestamp(timestamp);

        objectNode.put("message", "Playback loaded successfully.");

        output.add(objectNode);
    }
}
