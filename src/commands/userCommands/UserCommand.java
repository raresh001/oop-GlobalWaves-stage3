package commands.userCommands;

import commands.Command;
import fileio.input.CommandInput;

public abstract class UserCommand extends Command {
    protected final String username;
    protected UserCommand(final CommandInput commandInput) {
        super(commandInput);
        username = commandInput.getUsername();
    }
}
