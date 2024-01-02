package commands.userCommands.normalUserCommands.premiumCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

import java.util.HashMap;

public class BuyPremiumCommand extends NormalUserCommand {
    public BuyPremiumCommand(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("buyPremium", output);
        if (objectNode == null) {
            return;
        }

        if (normalUser.getPlayer(timestamp).isPremium()) {
            objectNode.put("message", username + " is already a premium user.");
        } else {
            normalUser.getPlayer().setTotalNoWatchedSongs(0);
            normalUser.getPlayer().setWatchedSongs(new HashMap<>());
            normalUser.getPlayer().setPremium(true);
            objectNode.put("message", username +
                                                    " bought the subscription successfully.");
        }

        output.add(objectNode);
    }
}
