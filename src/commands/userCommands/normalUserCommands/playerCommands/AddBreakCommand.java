package commands.userCommands.normalUserCommands.playerCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public class AddBreakCommand extends NormalUserCommand {
    private final double price;
    public AddBreakCommand(CommandInput commandInput) {
        super(commandInput);
        price = commandInput.getPrice();
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("adBreak", output);
        if (objectNode == null) {
            return;
        }

        if (normalUser.getPlayer(timestamp).getPlayingAudioFile() == null) {
            objectNode.put("message", username + " is not playing any music.");
        } else {
            normalUser.getPlayer().addBreak(price);
            objectNode.put("message", "Ad inserted successfully.");
        }

        output.add(objectNode);
    }
}
