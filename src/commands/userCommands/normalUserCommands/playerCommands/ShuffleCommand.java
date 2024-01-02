package commands.userCommands.normalUserCommands.playerCommands;

import audio.audioCollections.SongsCollection;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class ShuffleCommand extends NormalUserCommand {
    private final int seed;

    public ShuffleCommand(final CommandInput commandInput) {
        super(commandInput);
        if (commandInput.getSeed() != null) {
            seed = commandInput.getSeed();
        } else {
            seed = 0;
        }
    }

    /**
     * Executes shuffle command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("shuffle", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                    "Please load a source before using the shuffle function.");
            output.add(objectNode);
            return;
        }

        if (player.getPlayingAudioFile().getType() != PlayableEntity.AudioType.PLAYLIST
            && player.getPlayingAudioFile().getType() != PlayableEntity.AudioType.ALBUM) {
            objectNode.put("message", "The loaded source is not a playlist or an album.");
            output.add(objectNode);
            return;
        }

        if (player.getShuffle() != null) {
            player.setShuffle(null);
            objectNode.put("message", "Shuffle function deactivated successfully.");
            output.add(objectNode);
            return;
        }

        final ArrayList<Integer> shuffle = new ArrayList<>();
        for (int index = 0;
                index < ((SongsCollection) player.getPlayingAudioFile()).getSongs().size();
                index++) {
            shuffle.add(index);
        }

        Collections.shuffle(shuffle, new Random(seed));
        player.setShuffle(shuffle);
        objectNode.put("message", "Shuffle function activated successfully.");
        output.add(objectNode);
    }
}
