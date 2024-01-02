package commands.userCommands.normalUserCommands.playerCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;
import user.normalUser.player.Position;

public final class PrevCommand extends NormalUserCommand {
    public PrevCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    private void prevCommandSong(final Player player, final ObjectNode objectNode) {
        player.setPlaying(true);

        player.setPosition(new Position(0, 0));
        objectNode.put("message",
                "Returned to previous track successfully. The current track is "
                        + player.getPlayingAudioFile().getName()
                        + ".");
    }

    private void prevCommandPodcast(final Player player, final ObjectNode objectNode) {
        player.setPlaying(true);
        if (player.getPosition().getPositionInTrack() != 0) {
            player.setPosition(new Position(player.getPosition().getPositionInTrack(), 0));
        } else if (player.getPosition().getTrack() != 0) {
            player.setPosition(new Position(player.getPosition().getTrack() - 1, 0));
        } else {
            player.setPosition(new Position(0, 0));
        }

        objectNode.put("message",
                "Returned to previous track successfully. The current track is "
                        + player.getTrackName()
                        + ".");
    }

    private void prevCommandSongsCollection(final Player player, final ObjectNode objectNode) {
        player.setPlaying(true);

        if (player.getPosition().getPositionInTrack() != 0) {
            player.setPosition(new Position(player.getPosition().getTrack(), 0));
        } else {
            if (player.isShuffled()) {
                // get previous position of track
                int pos = player.getShuffle().indexOf(player.getPosition().getTrack());
                if (pos == 0) {
                    player.setPosition(new Position(player.getShuffle().get(0), 0));
                } else {
                    player.setPosition(new Position(player.getShuffle().get(pos - 1),
                            0));
                }
            } else {
                if (player.getPosition().getTrack() != 0) {
                    player.setPosition(new Position(player.getPosition().getTrack() - 1,
                            0));
                } else {
                    player.setPosition(new Position(0, 0));
                }
            }
        }

        objectNode.put("message",
                "Returned to previous track successfully. The current track is "
                        + player.getTrackName()
                        + ".");
    }

    /**
     * Executes prev command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("prev", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                        "Please load a source before returning to the previous track.");
            output.add(objectNode);
            return;
        }

        switch (player.getPlayingAudioFile().getType()) {
            case SONG:
                prevCommandSong(player, objectNode);
                break;
            case PODCAST:
                prevCommandPodcast(player, objectNode);
            default:
                prevCommandSongsCollection(player, objectNode);
        }

        output.add(objectNode);
    }
}
