package commands.userCommands.normalUserCommands.searchCommands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import fileio.input.FiltersInput;
import lombok.Getter;
import user.normalUser.player.Player;
import user.normalUser.search.bar.FilterFactory;
import user.normalUser.search.bar.SearchBar;
import user.normalUser.search.bar.SearchFilter;
import user.normalUser.search.bar.SearchableEntity;

@Getter
public final class SearchCommand extends NormalUserCommand {
    private final String type;
    private final FiltersInput filtersInput;

    public SearchCommand(final CommandInput commandInput) {
        super(commandInput);
        type = commandInput.getType();
        filtersInput = commandInput.getFilters();
    }

    /**
     * Executes search command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateNormalUserResult("search", output);
        if (objectNode == null) {
            return;
        }

        if (!normalUser.isOnline()) {
            objectNode.put("message", username + " is offline.");
            objectNode.set("results", (new ObjectMapper()).createArrayNode());
            output.add(objectNode);
            return;
        }

        // Reset the player first
        // retain the previous position of player before resetting (for podcasts)
        Player player = normalUser.getPlayer(timestamp);
        player.retainPreviousPosition();
        player.reset();
        player.setAdPrice(-1);

        // effectuate the search
        SearchBar searchBar = normalUser.getSearchBar();
        SearchFilter searchFilter = FilterFactory.createInstance(this);

        searchBar.reset();
        searchBar.setSearchedEntities(searchFilter.filter(normalUser));

        objectNode.put("message", "Search returned "
                                            + searchBar.getSearchedEntities().size()
                                            + " results");

        // Insert String array of results
        ArrayNode auxNode = (new ObjectMapper()).createArrayNode();
        for (SearchableEntity entity : searchBar.getSearchedEntities()) {
            auxNode.add(entity.getName());
        }

        objectNode.set("results", auxNode);
        output.add(objectNode);
    }
}
