package user.normalUser;

import lombok.Getter;

@Getter
public final class Notification {
    private final String name;
    private final String description;

    public Notification(final String name1, final String description1) {
        name = name1;
        description = description1;
    }

}
