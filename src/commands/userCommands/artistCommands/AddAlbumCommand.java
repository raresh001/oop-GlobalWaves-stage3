package commands.userCommands.artistCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.SongInput;
import user.normalUser.Notification;

import java.util.ArrayList;
import java.util.HashMap;

public final class AddAlbumCommand extends ArtistCommand {
    private final ArrayList<Song> songs = new ArrayList<>();
    private final String name;
    private final int releaseYear;
    private final String description;

    public AddAlbumCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        releaseYear = commandInput.getReleaseYear();
        description = commandInput.getDescription();
        for (SongInput songInput : commandInput.getSongs()) {
            songs.add(new Song(songInput));
        }
    }

    /**
     * Executes add album command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateArtistResult("addAlbum", output);
        if (objectNode == null) {
            return;
        }

        for (Album album : artist.getAlbums()) {
            if (album.getName().equals(name)) {
                objectNode.put("message", username
                                                    + " has another album with the same name.");
                output.add(objectNode);
                return;
            }
        }

        // check if there are any duplicates among the songs
        HashMap<String, Integer> albumSongs = new HashMap<>();
        for (Song song : songs) {
            if (albumSongs.containsKey(song.getName())) {
                objectNode.put("message", username
                                            + " has the same song at least twice in this album.");
                output.add(objectNode);
                return;
            }

            albumSongs.put(song.getName(), 1);
        }

        Album album = new Album(name, username, releaseYear, description, songs);

        Admin.getAlbums().add(album);
        artist.getAlbums().add(album);

        artist.notifySubscribers(new Notification("New Album",
                                        "New Album from " + artist.getName() + "."));
        objectNode.put("message", username + " has added new album successfully.");
        output.add(objectNode);
    }
}
