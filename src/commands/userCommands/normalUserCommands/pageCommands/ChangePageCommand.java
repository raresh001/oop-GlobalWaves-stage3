package commands.userCommands.normalUserCommands.pageCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.normalUser.page.HomePage;
import user.normalUser.page.LikedContentPage;

public final class ChangePageCommand extends NormalUserCommand {
    private final String nextPage;
    public ChangePageCommand(final CommandInput commandInput) {
        super(commandInput);
        nextPage = commandInput.getNextPage();
    }

    /**
     * Executes change page command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("changePage", output);
        if (objectNode == null) {
            return;
        }

        switch (nextPage) {
            case "Home":
                normalUser.setPage(new HomePage(normalUser));
                break;
            case "LikedContent":
                normalUser.setPage(new LikedContentPage(normalUser));
                break;
            default:
                objectNode.put("message", username
                                                    + " is trying to access a non-existent page.");
                output.add(objectNode);
                return;
        }

        objectNode.put("message", username + " accessed " + nextPage + " successfully.");
        output.add(objectNode);
    }
}
