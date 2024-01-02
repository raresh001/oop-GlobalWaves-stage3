package commands.userCommands.hostCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class RemoveAnnouncementCommand extends HostCommand {
    private final String name;

    public RemoveAnnouncementCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    /**
     * Executes remove announcement command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateHostResult("removeAnnouncement", output);
        if (objectNode == null) {
            return;
        }

        for (int announcementIter = 0;
             announcementIter < host.getAnnouncements().size();
             announcementIter++) {
            if (host.getAnnouncements().get(announcementIter).getName().equals(name)) {
                host.getAnnouncements().remove(announcementIter);
                objectNode.put("message",
                        username + " has successfully deleted the announcement.");
                output.add(objectNode);
                return;
            }
        }

        objectNode.put("message", username
                                            + " has no announcement with the given name.");
        output.add(objectNode);
    }
}
