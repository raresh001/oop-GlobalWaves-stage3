package user.normalUser.search.bar;

import admin.Admin;
import audio.audioCollections.Podcast;
import fileio.input.FiltersInput;
import user.normalUser.NormalUser;

import java.util.ArrayList;

public final class PodcastFilter implements SearchFilter {
    private final String name;
    private final String owner;

    PodcastFilter(final FiltersInput filtersInput) {
        this.name = filtersInput.getName();
        this.owner = filtersInput.getOwner();
    }

    private boolean respectsTheFilter(final Podcast podcast) {
        if (name != null && !podcast.getName().startsWith(name)) {
            return false;
        }

        return owner == null || podcast.getOwner().equals(owner);
    }

    @Override
    public ArrayList<SearchableEntity> filter(final NormalUser normalUser) {
        int currentIndex = 0;
        ArrayList<SearchableEntity> filteredList = new ArrayList<>();
        for (Podcast podcast : Admin.getPodcasts()) {
            if (respectsTheFilter(podcast)) {
                filteredList.add(podcast);
                currentIndex++;
                if (currentIndex == MAX_FILTERED_LIST_SIZE) {
                    return filteredList;
                }
            }
        }

        return filteredList;
    }
}
