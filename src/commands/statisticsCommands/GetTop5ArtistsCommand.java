package commands.statisticsCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import user.artist.Artist;

import java.util.ArrayList;

public final class GetTop5ArtistsCommand extends StatisticsCommand {
    public GetTop5ArtistsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    private int getTotalNoLikes(final Artist artist) {
        int number = 0;
        for (Album album : artist.getAlbums()) {
            for (Song song : album.getSongs()) {
                number += song.getLikes().size();
            }
        }
        return number;
    }


    /**
     * Executes get top 5 artist command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", "getTop5Artists");
        objectNode.put("timestamp", timestamp);

        ArrayList<Artist> topAlbums = new ArrayList<>();
        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();
        for (int i = 0; i < STATISTICS_RESULTS_MAX_LENGTH; i++) {
            Artist bestArtist = null;
            for (Artist artist : Admin.getArtists()) {
                if (topAlbums.contains(artist)) {
                    continue;
                }

                if (bestArtist == null || getTotalNoLikes(bestArtist) < getTotalNoLikes(artist)) {
                    bestArtist = artist;
                }
            }

            if (bestArtist == null) {
                break;
            }

            topAlbums.add(bestArtist);
            arrayNode.add(bestArtist.getName());
        }

        objectNode.set("result", arrayNode);
        output.add(objectNode);
    }
}
