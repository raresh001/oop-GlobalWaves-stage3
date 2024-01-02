package commands.statisticsCommands;

import admin.Admin;
import audio.audioCollections.Playlist;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

import java.util.ArrayList;

public final class GetTop5PlaylistsCommand extends StatisticsCommand {
    public GetTop5PlaylistsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * @return - an array list with the top 5 playlists (ranked by number of followers)
     */
    private static ArrayList<Playlist> getTop5Playlists() {
        final ArrayList<Playlist> topPlaylists = new ArrayList<>();
        for (int index = 0; index < STATISTICS_RESULTS_MAX_LENGTH; index++) {
            Playlist best = null;
            int bestNoFollows = -1;
            for (Playlist playlist : Admin.getPlaylists()) {
                if (!playlist.isPublic()) {
                    continue;
                }

                if (!topPlaylists.contains(playlist) && playlist.getNoFollowers() > bestNoFollows) {
                    bestNoFollows = playlist.getNoFollowers();
                    best = playlist;
                }
            }

            if (best == null) {
                return topPlaylists;
            }

            topPlaylists.add(best);
        }

        return topPlaylists;
    }

    /**
     * Executes get top 5 playlists command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();

        objectNode.put("command", "getTop5Playlists");
        objectNode.put("timestamp", timestamp);

        ArrayNode resultArray = objectMapper.createArrayNode();
        for (Playlist playlist : getTop5Playlists()) {
            resultArray.add(playlist.getName());
        }

        objectNode.set("result", resultArray);
        output.add(objectNode);
    }
}

