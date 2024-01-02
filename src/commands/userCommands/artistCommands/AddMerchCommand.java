package commands.userCommands.artistCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import user.artist.Merch;
import user.normalUser.Notification;

public final class AddMerchCommand extends ArtistCommand {
    private final String name;
    private final String description;
    private final int price;

    public AddMerchCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
        price = commandInput.getPrice();
    }

    /**
     * Executes add merch command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateArtistResult("addMerch", output);
        if (objectNode == null) {
            return;
        }

        for (Merch merch : artist.getMerchList()) {
            if (merch.getName().equals(name)) {
                objectNode.put("message", username
                                                    + " has merchandise with the same name.");
                output.add(objectNode);
                return;
            }
        }

        if (price < 0) {
            objectNode.put("message", "Price for merchandise can not be negative.");
            output.add(objectNode);
            return;
        }

        artist.notifySubscribers(new Notification("New Merch",
                "New Merch from " + artist.getName() + "."));
        artist.getMerchList().add(new Merch(name, description, price));
        objectNode.put("message", username + " has added new merchandise successfully.");
        output.add(objectNode);
    }
}
