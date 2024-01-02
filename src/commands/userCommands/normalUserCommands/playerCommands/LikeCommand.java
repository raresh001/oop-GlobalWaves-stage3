package commands.userCommands.normalUserCommands.playerCommands;

import audio.audioCollections.SongsCollection;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;

public final class LikeCommand extends NormalUserCommand {
    public LikeCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes like command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("like", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message", "Please load a source before liking or unliking.");
            output.add(objectNode);
            return;
        }

        Song song;
        switch (player.getPlayingAudioFile().getType()) {
            case SONG:
                song = (Song) player.getPlayingAudioFile();
                break;
            case PLAYLIST:
            case ALBUM:
                song = ((SongsCollection) player.getPlayingAudioFile())
                        .getSongs().get(player.getPosition().getTrack());
                break;
            default:
                objectNode.put("message", "Loaded source is not a song.");
                return;
        }

        song.like(normalUser, objectNode);
        output.add(objectNode);
    }
}
