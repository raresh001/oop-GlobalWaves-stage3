package user.normalUser.search.bar.filters;

import commands.userCommands.normalUserCommands.searchCommands.SearchCommand;
import user.normalUser.search.bar.InvalidSearchedEntityException;

public final class FilterFactory {
    private FilterFactory() {
    }

    /**
     * @param searchCommand - the command containing information about filter
     * @return - an instance of a class that implements SearchFilter
     *      according to the search command filter requirements
     */
    public static SearchFilter createInstance(final SearchCommand searchCommand)
        throws InvalidSearchedEntityException {
        if (searchCommand.getType().equals("song")) {
            return new SongFilter(searchCommand.getFiltersInput());
        }

        if (searchCommand.getType().equals("playlist")) {
            return new PlaylistFilter(searchCommand.getFiltersInput());
        }

        if (searchCommand.getType().equals("podcast")) {
            return new PodcastFilter(searchCommand.getFiltersInput());
        }

        if (searchCommand.getType().equals("album")) {
            return new AlbumFilter(searchCommand.getFiltersInput());
        }

        if (searchCommand.getType().equals("artist")) {
            return new ArtistFilter(searchCommand.getFiltersInput());
        }

        if (searchCommand.getType().equals("host")) {
            return new HostFilter(searchCommand.getFiltersInput());
        }

        throw new InvalidSearchedEntityException();
    }
}
