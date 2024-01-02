package user.normalUser.search.bar;

import admin.Admin;
import fileio.input.FiltersInput;
import user.artist.Artist;
import user.normalUser.NormalUser;

import java.util.ArrayList;

public final class ArtistFilter implements SearchFilter {
    private final String name;

    ArtistFilter(final FiltersInput filtersInput) {
        name = filtersInput.getName();
    }

    private boolean respectsTheFilter(final Artist artist) {
        return artist.getName().startsWith(name);
    }
    @Override
    public ArrayList<SearchableEntity> filter(final NormalUser normalUser) {
        ArrayList<SearchableEntity> filteredList = new ArrayList<>();
        for (Artist artist : Admin.getArtists()) {
            if (respectsTheFilter(artist)) {
                filteredList.add(artist);
                if (filteredList.size() == MAX_FILTERED_LIST_SIZE) {
                    break;
                }
            }
        }

        return filteredList;
    }
}
