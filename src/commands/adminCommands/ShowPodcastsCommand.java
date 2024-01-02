package commands.adminCommands;

import admin.Admin;
import audio.audioCollections.Podcast;
import audio.audioFiles.Episode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import user.host.Host;

public final class ShowPodcastsCommand extends Command {
    private final String user;
    public ShowPodcastsCommand(final CommandInput commandInput) {
        super(commandInput);
        user = commandInput.getUsername();
    }

    /**
     * Executes show podcasts command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = Command.createTemplateCommandResult("showPodcasts",
                user,
                timestamp);
        Host host = Admin.getHostByName(user);
        if (host == null) {
            objectNode.put("message", user + " doesn't exist or is not a host.");
            output.add(objectNode);
            return;
        }

        ArrayNode podcastsArray = (new ObjectMapper()).createArrayNode();
        for (Podcast podcast : host.getPodcasts()) {
            ObjectNode podcastObjectNode = (new ObjectMapper()).createObjectNode();
            podcastObjectNode.put("name", podcast.getName());

            ArrayNode episodesArray = (new ObjectMapper()).createArrayNode();
            for (Episode episode : podcast.getEpisodes()) {
                episodesArray.add(episode.getName());
            }

            podcastObjectNode.set("episodes", episodesArray);
            podcastsArray.add(podcastObjectNode);
        }

        objectNode.set("result", podcastsArray);
        output.add(objectNode);
    }
}
