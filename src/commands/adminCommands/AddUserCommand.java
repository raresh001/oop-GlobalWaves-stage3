package commands.adminCommands;

import admin.Admin;
import audio.audioCollections.Podcast;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.Command;
import fileio.input.CommandInput;
import user.artist.Artist;
import user.host.Host;
import user.normalUser.NormalUser;

public final class AddUserCommand extends Command {
    private final String type;
    private final String username;
    private final int age;
    private final String city;

    public AddUserCommand(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        username = commandInput.getUsername();
        age = commandInput.getAge();
        city = commandInput.getCity();
    }

    /**
     * Executes add user command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", "addUser");
        objectNode.put("user", username);
        objectNode.put("timestamp", timestamp);

        if (Admin.getUserByName(username) != null) {
            objectNode.put("message", "The username " + username + " is already taken.");
            output.add(objectNode);
            return;
        }

        switch (type) {
            case "user":
                Admin.getNormalUsers().add(new NormalUser(username, age, city));
                break;
            case "artist":
                Admin.getArtists().add(new Artist(username, age, city));
                break;
            case "host":
                Host host = new Host(username, age, city);
                for (Podcast podcast : Admin.getPodcasts()) {
                    if (podcast.getOwner().equals(host.getName())) {
                        host.getPodcasts().add(podcast);
                        podcast.setHost(host);
                    }
                }
                Admin.getHosts().add(host);
                break;
            default:
                objectNode.put("message", type + " is not a valid user type.");
                output.add(objectNode);
                return;
        }

        objectNode.put("message", "The username "
                                                + username
                                                + " has been added successfully.");
        output.add(objectNode);
    }
}
