package commands.userCommands.hostCommands;

import admin.Admin;
import audio.audioCollections.Podcast;
import audio.audioFiles.Episode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import fileio.input.EpisodeInput;
import user.normalUser.Notification;

import java.util.ArrayList;
import java.util.HashMap;

public final class AddPodcastCommand extends HostCommand {
    private final String name;
    private final ArrayList<Episode> episodes = new ArrayList<>();
    public AddPodcastCommand(final CommandInput commandInput) {
        super(commandInput);
        name = commandInput.getName();
        for (EpisodeInput episodeInput : commandInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
        }
    }

    /**
     * Executes add podcast command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateHostResult("addPodcast", output);
        if (objectNode == null) {
            return;
        }

        for (Podcast podcast : host.getPodcasts()) {
            if (podcast.getName().equals(name)) {
                objectNode.put("message", username
                        + " has another podcast with the same name.");
                output.add(objectNode);
                return;
            }
        }

        // check if there are any duplicates among the songs
        HashMap<String, Integer> podcastEpisodes = new HashMap<>();
        for (Episode episode : episodes) {
            if (podcastEpisodes.containsKey(episode.getName())) {
                objectNode.put("message", username
                        + " has the same episode at least twice in this podcast.");
                output.add(objectNode);
                return;
            }

            podcastEpisodes.put(episode.getName(), 1);
        }

        Podcast podcast = new Podcast(name, episodes, username);

        host.notifySubscribers(new Notification("New Podcast",
                "New Podcast from " + host.getName() + "."));
        Admin.getPodcasts().add(podcast);
        host.getPodcasts().add(podcast);

        objectNode.put("message", username + " has added new podcast successfully.");
        output.add(objectNode);
    }
}
