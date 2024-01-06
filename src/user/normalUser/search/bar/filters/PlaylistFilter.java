package user.normalUser.search.bar.filters;

import admin.Admin;
import audio.audioCollections.Playlist;
import fileio.input.FiltersInput;
import user.normalUser.NormalUser;
import user.normalUser.search.bar.SearchableEntity;

import java.util.ArrayList;

public final class PlaylistFilter implements SearchFilter {
    private final String name;
    private final String owner;

    PlaylistFilter(final FiltersInput filtersInput) {
        this.name = filtersInput.getName();
        this.owner = filtersInput.getOwner();
    }

    private boolean respectsTheFilter(final Playlist playlist) {
        if (name != null && !playlist.getName().startsWith(name)) {
            return false;
        }

        return owner == null || playlist.getOwner().equals(owner);
    }

    @Override
    public ArrayList<SearchableEntity> filter(final NormalUser normalUser) {
        int currentIndex = 0;
        ArrayList<SearchableEntity> filteredList = new ArrayList<>();

        for (Playlist playlist : normalUser.getPlaylists()) {
            if (respectsTheFilter(playlist)) {
                filteredList.add(playlist);
                currentIndex++;
                if (currentIndex == MAX_FILTERED_LIST_SIZE) {
                    return filteredList;
                }
            }
        }

        for (Playlist playlist : Admin.getPlaylists()) {
            if (playlist.getOwner().equals(normalUser.getName()) || !playlist.isPublic()) {
                continue;
            }
            if (respectsTheFilter(playlist)) {
                filteredList.add(playlist);
                currentIndex++;
                if (currentIndex == MAX_FILTERED_LIST_SIZE) {
                    return filteredList;
                }
            }
        }

        return filteredList;
    }
}

