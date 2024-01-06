package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public final class SeeMerchCommand extends NormalUserCommand {
    public SeeMerchCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("seeMerch", output);
        if (objectNode == null) {
            return;
        }

        ArrayNode arrayNode = (new ObjectMapper()).createArrayNode();
        for (String merchName : normalUser.getMerchNameList()) {
            arrayNode.add(merchName);
        }

        objectNode.set("result", arrayNode);
        output.add(objectNode);
    }
}
