package commands.statisticsCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

public final class GetTop5SongsCommand extends StatisticsCommand {
    public GetTop5SongsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * @return - an array list with the top 5 songs (ranked by number of likes)
     */
    private static ArrayList<Song> getTop5Songs() {
        final ArrayList<Song> topSongs = new ArrayList<>();
        for (int index = 0; index < STATISTICS_RESULTS_MAX_LENGTH; index++) {
            Song best = null;
            int bestNoLikes = -1;
            for (Song song : Admin.getSongs()) {
                int curNoLikes = song.getLikes().size();
                if (!topSongs.contains(song) && curNoLikes > bestNoLikes) {
                    bestNoLikes = curNoLikes;
                    best = song;
                }
            }

            for (Album album : Admin.getAlbums()) {
                for (Song song : album.getSongs()) {
                    int curNoLikes = song.getLikes().size();
                    if (!topSongs.contains(song) && curNoLikes > bestNoLikes) {
                        bestNoLikes = curNoLikes;
                        best = song;
                    }
                }
            }

            if (best == null) {
                return topSongs;
            }

            topSongs.add(best);
        }

        return topSongs;
    }

    /**
     * Executes get top 5 songs command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getTop5Songs");
        objectNode.put("timestamp", timestamp);

        ArrayNode resultArray = objectMapper.createArrayNode();
        for (Song song : getTop5Songs()) {
            resultArray.add(song.getName());
        }

        objectNode.set("result", resultArray);
        output.add(objectNode);
    }
}
