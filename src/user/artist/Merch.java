package user.artist;

import lombok.Getter;

@Getter
public final class Merch {
    private final String name;
    private final String description;
    private final int price;

    public Merch(final String name1, final String description1, final int price1) {
        this.name = name1;
        this.description = description1;
        this.price = price1;
    }
}
