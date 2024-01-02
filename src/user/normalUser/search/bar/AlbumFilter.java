package user.normalUser.search.bar;

import admin.Admin;
import audio.audioCollections.Album;
import fileio.input.FiltersInput;
import user.artist.Artist;
import user.normalUser.NormalUser;

import java.util.ArrayList;

public final class AlbumFilter implements SearchFilter {
    private final String name;
    private final String owner;
    private final String description;

    AlbumFilter(final FiltersInput filtersInput) {
        name = filtersInput.getName();
        owner = filtersInput.getOwner();
        description = filtersInput.getDescription();
    }

    private boolean respectsTheFilter(final Album album) {
        if (name != null && !album.getName().startsWith(name)) {
            return false;
        }

        if (owner != null && !album.getOwner().startsWith(owner)) {
            return false;
        }

        return description == null || album.getDescription().startsWith(description);
    }

    @Override
    public ArrayList<SearchableEntity> filter(final NormalUser normalUser) {
        ArrayList<SearchableEntity> filteredList = new ArrayList<>();

        for (Artist artist : Admin.getArtists()) {
            for (Album album : artist.getAlbums()) {
                if (respectsTheFilter(album)) {
                    filteredList.add(album);
                    if (filteredList.size() == MAX_FILTERED_LIST_SIZE) {
                        return filteredList;
                    }
                }
            }
        }

        return filteredList;
    }
}
