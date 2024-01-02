package user.artist;

import lombok.Getter;

@Getter
public final class Event {
    private final String name;
    private final String description;
    private final String date;

    public Event(final String name1, final String description1, final String date1) {
        name = name1;
        description = description1;
        date = date1;
    }
}
