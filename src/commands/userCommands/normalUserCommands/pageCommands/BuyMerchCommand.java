package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public class BuyMerchCommand extends NormalUserCommand {
    private final String name;
    public BuyMerchCommand(CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("buyMerch", output);
        if (objectNode == null) {
            return;
        }

        switch (normalUser.getPage().acceptBuyMerch(name)) {
            case INVALID_PAGE:
                objectNode.put("message", "Cannot buy merch from this page.");
                break;
            case NONEXISTENT_MERCH:
                objectNode.put("message", "The merch " + name + " doesn't exist.");
                break;
            default:
                normalUser.getMerchNameList().add(name);
                objectNode.put("message",
                                username + " has added new merch successfully.");
        }

        output.add(objectNode);
    }
}
