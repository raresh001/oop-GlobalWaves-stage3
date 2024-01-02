package user.normalUser.search.bar;

import user.normalUser.NormalUser;

import java.util.ArrayList;

public interface SearchFilter {
    int MAX_FILTERED_LIST_SIZE = 5;

    /**
     * @param normalUser - the user that commanded to search
     * @return an array list of audio entities that respect the filer
     */
    ArrayList<SearchableEntity> filter(NormalUser normalUser);
}
