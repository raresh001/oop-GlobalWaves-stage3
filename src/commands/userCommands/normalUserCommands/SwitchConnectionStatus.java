package commands.userCommands.normalUserCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class SwitchConnectionStatus extends NormalUserCommand {
    // store username to also address the case when it does not exist in the system
    private final String username;
    public SwitchConnectionStatus(final CommandInput commandInput) {
        super(commandInput);
        username = commandInput.getUsername();
    }

    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("switchConnectionStatus",
                                                                    output);

        if (objectNode == null) {
            return;
        }

        if (normalUser.isOnline()) {
            // update the player
            normalUser.getPlayer().updatePosition(timestamp);
        } else {
            // just update the timestamp
            normalUser.getPlayer().setTimestamp(timestamp);
        }

        normalUser.setOnline(!normalUser.isOnline());
        objectNode.put("message", username + " has changed status successfully.");
        output.add(objectNode);
    }
}
