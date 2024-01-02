package commands.adminCommands;

import admin.Admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import user.normalUser.NormalUser;

public final class GetOnlineUsersCommand extends Command {

    public GetOnlineUsersCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes get online users command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", "getOnlineUsers");

        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();
        for (NormalUser normalUser : Admin.getNormalUsers()) {
            if (normalUser.isOnline()) {
                arrayNode.add(normalUser.getName());
            }
        }

        objectNode.set("result", arrayNode);
        objectNode.put("timestamp", timestamp);
        output.add(objectNode);
    }
}
