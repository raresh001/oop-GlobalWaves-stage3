package commands.userCommands.normalUserCommands.playlistCommands;

import audio.audioCollections.Playlist;
import audio.audioCollections.SongsCollection;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.player.Player;

public final class AddRemoveInPlaylistCommand extends NormalUserCommand {
    private final int playlistId;

    public AddRemoveInPlaylistCommand(final CommandInput commandInput) {
        super(commandInput);
        playlistId = commandInput.getPlaylistId();
    }

    /**
     * Executes add-remove in playlist command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("addRemoveInPlaylist", output);
        if (objectNode == null) {
            return;
        }

        Player player = normalUser.getPlayer(timestamp);

        if (player.getPlayingAudioFile() == null) {
            objectNode.put("message",
                        "Please load a source before adding to or removing from the playlist.");
            output.add(objectNode);
            return;
        }

        if (normalUser.getPlaylists().size() < playlistId) {
            objectNode.put("message", "The specified playlist does not exist.");
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
                            .getSongs()
                            .get(player.getPosition().getTrack());
                break;
            default:
                objectNode.put("message", "The loaded source is not a song.");
                output.add(objectNode);
                return;
        }

        Playlist playlist = normalUser.getPlaylists().get(playlistId - 1);
        if (!playlist.getSongs().contains(song)) {
            playlist.getSongs().add(song);
            song.getPlaylistList().add(playlist);
            objectNode.put("message", "Successfully added to playlist.");
        } else {
            playlist.getSongs().remove(song);
            song.getPlaylistList().remove(playlist);
            objectNode.put("message", "Successfully removed from playlist.");
        }

        output.add(objectNode);
    }
}
