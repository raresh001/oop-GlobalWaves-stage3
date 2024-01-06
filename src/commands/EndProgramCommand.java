package commands;

import admin.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import user.artist.Artist;
import user.normalUser.NormalUser;

import java.util.ArrayList;

public final class EndProgramCommand {

    private EndProgramCommand() {
    }

    /**
     * Return statistics about the artists at the end of the execution
     * @param timestamp - the final timestamp
     * @param output - contains the result of the command
     */
    public static void executeCommand(final int timestamp, final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", "endProgram");

        Admin.updatePlayers(timestamp);
        for (NormalUser normalUser : Admin.getNormalUsers()) {
            if (normalUser.getPlayer(timestamp).isPremium()) {
                normalUser.cancelPremium();
            }
        }

        Admin.getArtists().removeIf(Artist::hasNotPlaysOrMerch);

        ArrayList<Artist> artist = new ArrayList<>(Admin.getArtists());

        artist.sort((o1, o2) -> o1.getTotalRevenue() == o2.getTotalRevenue()
                                    ? o1.getName().compareTo(o2.getName())
                                        : (int) (o2.getTotalRevenue() - o1.getTotalRevenue()));

        ObjectNode statsObject = (new ObjectMapper()).createObjectNode();
        for (int artistIndex = 0; artistIndex < artist.size(); artistIndex++) {
            statsObject.set(artist.get(artistIndex).getName(),
                            artist.get(artistIndex).showStatistics(artistIndex));
        }

        objectNode.set("result", statsObject);
        output.add(objectNode);
    }
}
