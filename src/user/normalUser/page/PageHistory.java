package user.normalUser.page;

import java.util.LinkedList;

public final class PageHistory {
    private LinkedList<Page> undo = new LinkedList<>();
    private LinkedList<Page> redo = new LinkedList<>();

    /**
     * change the current page
     * @param page - the page that is changed (and put in undo stack)
     */
    public void changePage(final Page page) {
        redo = new LinkedList<>();
        undo.push(page);
    }

    /**
     * undo the last operation
     * @param previous - the page before undo (that is pushed to redo stack)
     * @return - the current page
     */
    public Page undo(final Page previous) {
        if (undo.isEmpty()) {
            return null;
        }

        Page page = undo.pop();
        redo.push(previous);
        return page;
    }

    /**
     * redo the last operation
     * @param previous - the page before redo (that is pushed to undo stack)
     * @return - the current page
     */
    public Page redo(final Page previous) {
        if (redo.isEmpty()) {
            return null;
        }

        Page page = redo.pop();
        undo.push(previous);
        return page;
    }
}
