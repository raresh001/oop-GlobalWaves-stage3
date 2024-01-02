package commands.userCommands.normalUserCommands.searchCommands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.artist.Artist;
import user.host.Host;
import user.normalUser.page.ArtistPage;
import user.normalUser.page.HostPage;
import user.normalUser.player.PlayableEntity;
import user.normalUser.search.bar.SearchBar;
import user.normalUser.search.bar.SearchableEntity;

public final class SelectCommand extends NormalUserCommand {
    private final int iterNumber;

    public SelectCommand(final CommandInput commandInput) {
        super(commandInput);
        iterNumber = commandInput.getItemNumber();
    }

    /**
     * select the current entity (for load)
     * @param playableEntity - the selected playable entity
     * @param objectNode - the json containing the result of this command
     */
    public void select(final PlayableEntity playableEntity, final ObjectNode objectNode) {
        normalUser.getSearchBar().setSelectedEntity(playableEntity);
        normalUser.getSearchBar().setSelected(true);
        objectNode.put("message",
                "Successfully selected " + playableEntity.getName() + ".");
    }

    /**
     * select the current entity (select his page)
     * @param artist - the selected artist
     * @param objectNode - the json containing the result of this command
     */
    public void select(final Artist artist, final ObjectNode objectNode) {
        normalUser.setPage(new ArtistPage(artist));
        objectNode.put("message",
                "Successfully selected " + artist.getName() + "'s page.");
    }

    /**
     * select the current entity (select his page)
     * @param host - the selected host
     * @param objectNode - the json containing the result of this command
     */
    public void select(final Host host, final ObjectNode objectNode) {
        normalUser.setPage(new HostPage(host));
        objectNode.put("message",
                "Successfully selected " + host.getName() + "'s page.");
    }

    /**
     * Executes select command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("select", output);
        if (objectNode == null) {
            return;
        }

        SearchBar searchBar = normalUser.getSearchBar();

        if (searchBar.getSearchedEntities() == null) {
            objectNode.put("message",
                    "Please conduct a search before making a selection.");

            searchBar.setSelected(false);
            output.add(objectNode);
            return;
        }

        if (iterNumber - 1 >= searchBar.getSearchedEntities().size()) {
            objectNode.put("message", "The selected ID is too high.");
            searchBar.setSelected(false);
            output.add(objectNode);
            return;
        }

        SearchableEntity entity = searchBar.getSearchedEntities().get(iterNumber - 1);
        // accept this command conforming to the type of selected entity
        entity.acceptSelect(this, objectNode);
        output.add(objectNode);
    }
}
