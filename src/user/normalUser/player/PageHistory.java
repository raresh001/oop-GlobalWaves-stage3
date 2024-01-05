package user.normalUser.player;

import user.normalUser.page.Page;
import java.util.LinkedList;

public final class PageHistory {
    private LinkedList<Page> undo = new LinkedList<>();
    private LinkedList<Page> redo = new LinkedList<>();

    public void changePage(final Page page) {
        redo = new LinkedList<>();
        undo.push(page);
    }

    public Page undo() {
        if (undo.isEmpty()) {
            return null;
        }

        Page page = undo.pop();
        redo.push(page);
        return page;
    }

    public Page redo() {
        if (redo.isEmpty()) {
            return null;
        }

        Page page = redo.pop();
        undo.push(page);
        return page;
    }
}
