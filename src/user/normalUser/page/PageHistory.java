package user.normalUser.page;

import user.normalUser.page.Page;
import java.util.LinkedList;

public final class PageHistory {
    private LinkedList<Page> undo = new LinkedList<>();
    private LinkedList<Page> redo = new LinkedList<>();

    public void changePage(final Page page) {
        redo = new LinkedList<>();
        undo.push(page);
    }

    public Page undo(final Page previous) {
        if (undo.isEmpty()) {
            return null;
        }

        Page page = undo.pop();
        redo.push(previous);
        return page;
    }

    public Page redo(final Page previous) {
        if (redo.isEmpty()) {
            return null;
        }

        Page page = redo.pop();
        undo.push(previous);
        return page;
    }
}
