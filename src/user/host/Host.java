package user.host;

import admin.Admin;
import audio.audioCollections.Podcast;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.WrappedCommand;
import commands.userCommands.normalUserCommands.searchCommands.SelectCommand;
import lombok.Getter;
import user.User;
import user.normalUser.NormalUser;
import user.normalUser.Notification;
import user.normalUser.Subject;
import user.normalUser.player.Listenable;
import user.normalUser.search.bar.SearchableEntity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Getter
public final class Host extends User implements SearchableEntity {
    private final List<Podcast> podcasts = new LinkedList<>();
    private final List<Announcement> announcements = new ArrayList<>();

    private final Subject subject = new Subject();

    public Host(final String name1, final int age1, final String city1) {
        super(name1, age1, city1);
    }

    @Override
    public void acceptDelete() {
        Admin.removeUser(this);
    }

    @Override
    public boolean canBeDeleted() {
        for (NormalUser normalUser : Admin.getNormalUsers()) {
            if (normalUser.getPage().isCreatedByUser(getName())) {
                return false;
            }
        }

        for (Podcast podcast : podcasts) {
            if (!podcast.canBeDeleted()) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void acceptSelect(final SelectCommand selectCommand, final ObjectNode objectNode) {
        selectCommand.select(this, objectNode);
    }

    @Override
    public WrappedCommand.wrapResult acceptWrap(final WrappedCommand wrappedCommand, final ObjectNode objectNode) {
        return wrappedCommand.wrap(this, objectNode);
    }

    public void notifySubscribers(Notification notification) {
        subject.notify(notification);
    }
}
