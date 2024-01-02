package commands.userCommands.normalUserCommands.playerCommands;

import audio.audioCollections.Podcast;
import audio.audioCollections.SongsCollection;
import audio.audioFiles.Episode;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;
import user.normalUser.player.Position;

import java.util.ArrayList;
import java.util.List;

public final class NextCommand extends NormalUserCommand {
    public NextCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    private void nextCommandSong(final Player player, final ObjectNode objectNode) {
        player.setPlaying(true);
        switch (player.getRepeat()) {
            case 0:
                player.reset();
                objectNode.put("message",
                        "Please load a source before skipping to the next track.");
                return;
            case 1:
                player.resetRepeat();
                // Intentionally missed return
            default:
                player.setPlaying(true);
                player.setPosition(new Position(0, 0));
                objectNode.put("message",
                        "Skipped to next track successfully. The current track is "
                                + player.getPlayingAudioFile().getName()
                                + ".");
        }
    }

    private void nextCommandPodcast(final Player player, final ObjectNode objectNode) {
        player.setPlaying(true);
        ArrayList<Episode> episodes = ((Podcast) player.getPlayingAudioFile()).getEpisodes();
        int track = player.getPosition().getTrack() + 1;
        if (track == episodes.size()) {
            switch (player.getRepeat()) {
                case 0:
                    player.reset();
                    objectNode.put("message",
                            "Please load a source before skipping to the next track.");
                    return;
                case 1:
                    player.resetRepeat();
                    // Intentionally missed return
                default:
                    track = 0;
            }
        }

        player.setPosition(new Position(track, 0));
        objectNode.put("message",
                "Skipped to next track successfully. The current track is "
                        + episodes.get(track).getName()
                        + ".");
    }

    private void nextCommandSongsCollection(final Player player, final ObjectNode objectNode) {
        List<Song> songs = ((SongsCollection) player.getPlayingAudioFile()).getSongs();

        player.setPlaying(true);
        if (player.getRepeat() == 2) {
            player.setPosition(new Position(player.getPosition().getTrack(), 0));
            objectNode.put("message",
                    "Skipped to next track successfully. The current track is "
                            + songs.get(player.getPosition().getTrack()).getName()
                            + ".");
            return;
        }

        if (player.isShuffled()) {
            int posInShuffle = player.getShuffle().indexOf(player.getPosition().getTrack());
            if (posInShuffle == songs.size() - 1) {
                if (player.getRepeat() == 1) {
                    posInShuffle = -1;
                } else {
                    player.reset();
                    objectNode.put("message",
                            "Please load a source before skipping to the next track.");
                    return;
                }
            }
            player.setPosition(new Position(player.getShuffle().get(1 + posInShuffle),
                    0));
        } else {
            int track = player.getPosition().getTrack();
            if (track == songs.size() - 1) {
                if (player.getRepeat() == 1) {
                    track = -1;
                } else {
                    player.reset();
                    objectNode.put("message",
                            "Please load a source before skipping to the next track.");
                    return;
                }
            }

            player.setPosition(new Position(track + 1, 0));
        }

        objectNode.put("message",
                "Skipped to next track successfully. The current track is "
                        + player.getTrackName() + ".");
    }

    /**
     * Executes next command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("next", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                    "Please load a source before skipping to the next track.");
            output.add(objectNode);
            return;
        }

        switch (player.getPlayingAudioFile().getType()) {
            case SONG:
                if (player.getPlayingAudioFile().getName().equals("adBreak")) {
                    objectNode.put("message", "Cannot skip add");
                    output.add(objectNode);
                    return;
                }

                nextCommandSong(player, objectNode);
                break;
            case PODCAST:
                nextCommandPodcast(player, objectNode);
                break;
            default:
                nextCommandSongsCollection(player, objectNode);
        }

        output.add(objectNode);
    }
}
