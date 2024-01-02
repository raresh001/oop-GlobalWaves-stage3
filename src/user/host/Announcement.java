package user.host;

import lombok.Getter;

@Getter
public final class Announcement {
    private final String name;
    private final String description;

    public Announcement(final String name1, final String description1) {
        name = name1;
        description = description1;
    }
}
