package commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.input.CommandInput;

public final class InvalidCommand extends Command {
    public InvalidCommand(final CommandInput commandInput) {
        super(commandInput);
    }

    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = (new ObjectMapper()).createObjectNode();
        output.add(objectNode.put("message", "invalid command"));
    }
}
