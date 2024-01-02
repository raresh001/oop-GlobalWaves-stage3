package commands.userCommands.artistCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class RemoveEventCommand extends ArtistCommand {
    private final String name;
    public RemoveEventCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    /**
     * Executes remove event command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateArtistResult("removeEvent", output);
        if (objectNode == null) {
            return;
        }

        for (int eventIndex = 0; eventIndex < artist.getEvents().size(); eventIndex++) {
            if (artist.getEvents().get(eventIndex).getName().equals(name)) {
                artist.getEvents().remove(eventIndex);
                objectNode.put("message", username + " deleted the event successfully.");
                output.add(objectNode);
                return;
            }
        }

        objectNode.put("message", username
                                            + " doesn't have an event with the given name.");
        output.add(objectNode);
    }
}
