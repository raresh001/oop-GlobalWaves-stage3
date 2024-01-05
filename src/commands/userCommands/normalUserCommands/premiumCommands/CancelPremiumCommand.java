package commands.userCommands.normalUserCommands.premiumCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public class CancelPremiumCommand extends NormalUserCommand {
    public CancelPremiumCommand(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("cancelPremium", output);
        if (objectNode == null) {
            return;
        }

        if (!normalUser.getPlayer(timestamp).isPremium()) {
            objectNode.put("message", username + " is not a premium user.");
        } else {
            normalUser.cancelPremium();
            objectNode.put("message", username +
                    " cancelled the subscription successfully.");
        }

        output.add(objectNode);
    }
}
