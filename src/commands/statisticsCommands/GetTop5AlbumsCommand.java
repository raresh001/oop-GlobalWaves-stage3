package commands.statisticsCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;
import java.util.Comparator;

public final class GetTop5AlbumsCommand extends StatisticsCommand {
    public GetTop5AlbumsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    private static int getTotalNoLikes(final Album album) {
        int number = 0;
        for (Song song : album.getSongs()) {
            number += song.getLikes().size();
        }

        return number;
    }

    /**
     * Executes get top 5 albums command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", "getTop5Albums");
        objectNode.put("timestamp", timestamp);

        ArrayList<Album> topAlbums = new ArrayList<>();
        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();

        for (int i = 0; i < STATISTICS_RESULTS_MAX_LENGTH; i++) {
            Comparator<Album> albumComparator = (album1, album2) -> {
                if (getTotalNoLikes(album1) != getTotalNoLikes(album2)) {
                    // the first album in the array is the one with the most number of likes
                    return getTotalNoLikes(album2) - getTotalNoLikes(album1);
                }

                return album1.getName().compareTo(album2.getName());
            };

            Album bestAlbum = null;
            for (Album album : Admin.getAlbums()) {
                if (topAlbums.contains(album)) {
                    continue;
                }

                if (bestAlbum == null || albumComparator.compare(bestAlbum, album) > 0) {
                    bestAlbum = album;
                }
            }

            if (bestAlbum == null) {
                break;
            }

            topAlbums.add(bestAlbum);
            arrayNode.add(bestAlbum.getName());
        }

        objectNode.set("result", arrayNode);
        output.add(objectNode);
    }
}
