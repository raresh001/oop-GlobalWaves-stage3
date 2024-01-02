package commands.adminCommands;

import admin.Admin;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import user.User;

public final class DeleteUserCommand extends Command {
    private final String username;
    public DeleteUserCommand(final CommandInput commandInput) {
        super(commandInput);
        username = commandInput.getUsername();
    }

    /**
     * Executes delete user command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = Command.createTemplateCommandResult("deleteUser",
                                                                    username,
                                                                    timestamp);

        User user = Admin.getUserByName(username);

        if (user == null) {
            objectNode.put("message", "The username " + username + " doesn't exist.");
            output.add(objectNode);
            return;
        }

        Admin.updatePlayers(timestamp);

        if (!user.canBeDeleted()) {
            objectNode.put("message", username + " can't be deleted.");
            output.add(objectNode);
            return;
        }

        user.acceptDelete();
        objectNode.put("message", username + " was successfully deleted.");
        output.add(objectNode);
    }
}
