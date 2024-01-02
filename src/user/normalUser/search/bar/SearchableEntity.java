package user.normalUser.search.bar;

import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.searchCommands.SelectCommand;

public interface SearchableEntity {
    /**
     * @return the name of the entity
     */
    String getName();

    /**
     * accept selection (visit), conforming to the type of searchable entity
     * @param selectCommand - the command that selects this entity
     * @param objectNode - the json containing the result of this command
     */
    void acceptSelect(SelectCommand selectCommand, ObjectNode objectNode);
}
