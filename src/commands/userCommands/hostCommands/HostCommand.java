package commands.userCommands.hostCommands;

import admin.Admin;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.userCommands.UserCommand;
import fileio.input.CommandInput;
import user.host.Host;

public abstract class HostCommand extends UserCommand {
    protected final Host host;
    protected HostCommand(final CommandInput commandInput) {
        super(commandInput);
        host = Admin.getHostByName(username);
    }

    protected final ObjectNode createTemplateHostResult(final String command,
                                                        final ArrayNode output) {
        ObjectNode objectNode = Command.createTemplateCommandResult(command,
                username,
                timestamp);

        if (host != null) {
            return objectNode;
        }

        if (Admin.getArtistByName(username) != null
                || Admin.getNormalUserByName(username) != null) {
            objectNode.put("message", username + " is not a host.");
            output.add(objectNode);
            return null;
        }

        objectNode.put("message", "The username " + username + " doesn't exist.");
        output.add(objectNode);
        return null;
    }
}
