package user.normalUser.search.bar;

import lombok.Getter;
import lombok.Setter;
import user.normalUser.player.PlayableEntity;

import java.util.ArrayList;

@Getter
@Setter
public final class SearchBar {
    private ArrayList<SearchableEntity> searchedEntities;
    private PlayableEntity selectedEntity;
    private boolean isSelected;

    /**
     * resets the search bar
     */
    public void reset() {
        searchedEntities = null;
        selectedEntity = null;
        isSelected = false;
    }
}
