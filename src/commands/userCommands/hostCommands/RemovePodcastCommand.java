package commands.userCommands.hostCommands;

import admin.Admin;
import audio.audioCollections.Podcast;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class RemovePodcastCommand extends HostCommand {
    private final String name;
    public RemovePodcastCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
    }

    /**
     * Executes remove podcast command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateHostResult("removePodcast", output);
        if (objectNode == null) {
            return;
        }

        Admin.updatePlayers(timestamp);

        for (Podcast podcast : host.getPodcasts()) {
            if (podcast.getName().equals(name)) {
                Admin.updatePlayers(timestamp);

                if (!podcast.canBeDeleted()) {
                    objectNode.put("message", username + " can't delete this podcast.");
                    output.add(objectNode);
                    return;
                }

                host.getPodcasts().remove(podcast);
                objectNode.put("message", username
                        + " deleted the podcast successfully.");
                output.add(objectNode);
                return;
            }
        }

        objectNode.put("message", username
                                            + " doesn't have a podcast with the given name.");
        output.add(objectNode);
    }
}
