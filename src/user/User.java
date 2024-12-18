package user;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.WrappedCommand;
import fileio.input.UserInput;
import lombok.Getter;

@Getter
public abstract class User {
    private final String name;
    private final int age;
    private final String city;

    public User(final UserInput userInput) {
        name = userInput.getUsername();
        age = userInput.getAge();
        city = userInput.getCity();
    }

    public User(final String name1, final int age1, final String city1) {
        name = name1;
        age = age1;
        city = city1;
    }

    /**
     * Accept removeUser (visit) from Admin, corresponding to the user's type
     */
    public abstract void acceptDelete();

    /**
     * @return - a boolean indicating whether this user can be deleted
     * (if any of his files/ his page is currently being watched
     */
    public abstract boolean canBeDeleted();

    /**
     * Accept the wrapping of this command (visit)
     * @param wrappedCommand - the wrapping command that analyzes this user
     * @param objectNode - the object node containing the statistics of this action
     * @return - the success rate of this operation (if there was any information to be put)
     */
    public abstract WrappedCommand.WrapResult acceptWrap(WrappedCommand wrappedCommand,
                                                         ObjectNode objectNode);
}
