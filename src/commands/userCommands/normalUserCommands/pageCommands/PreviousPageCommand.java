package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.page.ArtistPage;
import user.normalUser.page.LikedContentPage;
import user.normalUser.page.Page;

import java.util.LinkedList;

public final class PreviousPageCommand extends NormalUserCommand {
    public PreviousPageCommand(CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("previousPage", output);
        if (objectNode == null) {
            return;
        }

        Page previousPage = normalUser.getPageHistory().undo(normalUser.getPage());
        if (previousPage == null) {
            objectNode.put("message", "There are no pages left to go back.");
        } else {
            normalUser.setPage(previousPage);
            objectNode.put("message", "The user "
                                            + username
                                            + " has navigated successfully to the previous page.");
        }

        output.add(objectNode);
    }
}
