package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.page.Page;

public class NextPageCommand extends NormalUserCommand {
    public NextPageCommand(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("nextPage", output);
        if (objectNode == null) {
            return;
        }

        Page previousPage = normalUser.getPageHistory().redo(normalUser.getPage());
        if (previousPage == null) {
            objectNode.put("message", "There are no pages left to go forward.");
        } else {
            normalUser.setPage(previousPage);
            objectNode.put("message", "The user "
                    + username
                    + " has navigated successfully to the next page.");
        }

        output.add(objectNode);
    }
}
