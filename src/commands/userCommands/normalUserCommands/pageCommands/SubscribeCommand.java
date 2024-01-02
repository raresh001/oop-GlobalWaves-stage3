package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public class SubscribeCommand extends NormalUserCommand {
    public SubscribeCommand(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("subscribe", output);
        if (objectNode == null) {
            return;
        }

        objectNode.put("message", normalUser.getPage().acceptSubscribe(normalUser));
        output.add(objectNode);
    }
}
