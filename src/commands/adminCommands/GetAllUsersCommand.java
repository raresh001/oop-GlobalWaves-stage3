package commands.adminCommands;

import admin.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import user.artist.Artist;
import user.host.Host;
import user.normalUser.NormalUser;

public final class GetAllUsersCommand extends Command {
    public GetAllUsersCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes get all users command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", "getAllUsers");

        // put all users' names in arrayNode
        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();
        for (NormalUser normalUser: Admin.getNormalUsers()) {
            arrayNode.add(normalUser.getName());
        }
        for (Artist artist : Admin.getArtists()) {
            arrayNode.add(artist.getName());
        }
        for (Host host : Admin.getHosts()) {
            arrayNode.add(host.getName());
        }

        objectNode.set("result", arrayNode);
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }
}
