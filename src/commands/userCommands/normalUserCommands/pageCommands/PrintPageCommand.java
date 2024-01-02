package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;

public final class PrintPageCommand extends NormalUserCommand {
    public PrintPageCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    /**
     * Executes print page command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("printCurrentPage",
                                                                    output);
        if (objectNode == null) {
            return;
        }

        objectNode.put("message", normalUser.getPage().printPage());
        output.add(objectNode);
    }
}
