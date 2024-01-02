package commands.userCommands.hostCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import user.host.Announcement;
import user.normalUser.Notification;

public final class AddAnnouncementCommand extends HostCommand {
    private final String name;
    private final String description;
    public AddAnnouncementCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        description = commandInput.getDescription();
    }

    /**
     * Executes add announcement command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateHostResult("addAnnouncement", output);
        if (objectNode == null) {
            return;
        }

        for (Announcement announcement : host.getAnnouncements()) {
            if (announcement.getName().equals(name)) {
                objectNode.put("message", username
                                        + " has already added an announcement with this name.");
                output.add(objectNode);
                return;
            }
        }

        host.notifySubscribers(new Notification("New Announcement",
                "New Announcement from " + host.getName() + "."));
        host.getAnnouncements().add(new Announcement(name, description));
        objectNode.put("message", username
                + " has successfully added new announcement.");
        output.add(objectNode);
    }
}
