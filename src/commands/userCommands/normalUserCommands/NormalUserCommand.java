package commands.userCommands.normalUserCommands;

import admin.Admin;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import commands.userCommands.UserCommand;
import fileio.input.CommandInput;
import user.normalUser.NormalUser;

public abstract class NormalUserCommand extends UserCommand {
    protected final NormalUser normalUser;
    protected NormalUserCommand(final CommandInput commandInput) {
        super(commandInput);
        normalUser = Admin.getNormalUserByName(username);
    }

    protected final ObjectNode createTemplateNormalUserResult(final String command,
                                                              final ArrayNode output) {
        ObjectNode objectNode = Command.createTemplateCommandResult(command,
                                                                    username,
                                                                    timestamp);

        if (normalUser != null) {
            return objectNode;
        }

        if (Admin.getArtistByName(username) != null || Admin.getHostByName(username) != null) {
            objectNode.put("message", username + " is not a normal user.");
            output.add(objectNode);
            return null;
        }

        objectNode.put("message", "The username " + username + " doesn't exist.");
        output.add(objectNode);
        return null;

    }

    protected final ObjectNode createTemplateResultRequireOnline(final String command,
                                                                 final ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult(command, output);
        if (objectNode == null) {
            return null;
        }

        if (!normalUser.isOnline()) {
            objectNode.put("message", username + " is offline.");
            output.add(objectNode);
            return null;
        }

        return objectNode;
    }
}
