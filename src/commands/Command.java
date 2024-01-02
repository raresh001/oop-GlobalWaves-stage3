package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;
import lombok.Getter;

public abstract class Command {
    protected final int timestamp;

    protected Command(final CommandInput commandInput) {
        timestamp = commandInput.getTimestamp();
    }

    /**
     * executes the command and writes the result in output
     * @param output - the ArrayNode containing command's result
     */
    public abstract void executeCommand(ArrayNode output);

    /**
     * @param commandName - the command's name
     * @param username - the name of the user who did the command
     * @param timestamp - the moment when the command was done
     * @return - a template command result, containing the command name, the
     *      username and the timestamp
     */
    public static ObjectNode createTemplateCommandResult(final String commandName,
                                                         final String username,
                                                         final int timestamp) {
        final ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        objectNode.put("command", commandName);
        objectNode.put("user", username);
        objectNode.put("timestamp", timestamp);

        return objectNode;
    }
}
