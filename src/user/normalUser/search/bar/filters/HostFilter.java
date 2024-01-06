package user.normalUser.search.bar.filters;

import admin.Admin;
import fileio.input.FiltersInput;
import user.host.Host;
import user.normalUser.NormalUser;
import user.normalUser.search.bar.SearchableEntity;

import java.util.ArrayList;

public final class HostFilter implements SearchFilter {
    private final String name;

    HostFilter(final FiltersInput filtersInput) {
        name = filtersInput.getName();
    }

    private boolean respectsTheFilter(final Host host) {
        return host.getName().startsWith(name);
    }
    @Override
    public ArrayList<SearchableEntity> filter(final NormalUser normalUser) {
        ArrayList<SearchableEntity> filteredList = new ArrayList<>();
        for (Host host : Admin.getHosts()) {
            if (respectsTheFilter(host)) {
                filteredList.add(host);
                if (filteredList.size() == MAX_FILTERED_LIST_SIZE) {
                    break;
                }
            }
        }

        return filteredList;
    }
}
