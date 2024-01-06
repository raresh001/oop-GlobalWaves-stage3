package commands.userCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.Notification;

import java.util.ArrayList;

public final class GetNotificationsCommand extends NormalUserCommand {
    public GetNotificationsCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("getNotifications",
                                                                    output);
        if (objectNode == null) {
            return;
        }

        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();
        for (Notification notification : normalUser.getNotifications()) {
            ObjectNode notificationObjectNode = (new ObjectMapper()).createObjectNode();
            notificationObjectNode.put("name", notification.getName())
                                    .put("description", notification.getDescription());
            arrayNode.add(notificationObjectNode);
        }

        objectNode.set("notifications", arrayNode);
        output.add(objectNode);

        // reset notifications
        normalUser.setNotifications(new ArrayList<>());
    }
}
