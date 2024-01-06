package user.normalUser.page;

import user.artist.Artist;
import user.normalUser.NormalUser;

public final class ArtistPage implements Page {
    private final Artist artist;

    public ArtistPage(final Artist artist1) {
        artist = artist1;
    }
    @Override
    public String printPage() {
        final StringBuilder builder = new StringBuilder("Albums:\n\t[");

        if (!artist.getAlbums().isEmpty()) {
            builder.append(artist.getAlbums().get(0).getName());
        }
        for (int albumIndex = 1; albumIndex < artist.getAlbums().size(); albumIndex++) {
            builder.append(", ").append(artist.getAlbums().get(albumIndex).getName());
        }

        builder.append("]\n\nMerch:\n\t[");

        if (!artist.getMerchList().isEmpty()) {
            builder.append(artist.getMerchList().getFirst().getName())
                    .append(" - ")
                    .append(artist.getMerchList().getFirst().getPrice())
                    .append(":\n\t")
                    .append(artist.getMerchList().getFirst().getDescription());
        }
        for (int merchIndex = 1; merchIndex < artist.getMerchList().size(); merchIndex++) {
            builder.append(", ")
                    .append(artist.getMerchList().get(merchIndex).getName())
                    .append(" - ")
                    .append(artist.getMerchList().get(merchIndex).getPrice())
                    .append(":\n\t")
                    .append(artist.getMerchList().get(merchIndex).getDescription());
        }

        builder.append("]\n\nEvents:\n\t[");

        if (!artist.getEvents().isEmpty()) {
            builder.append(artist.getEvents().getFirst().getName())
                    .append(" - ")
                    .append(artist.getEvents().getFirst().getDate())
                    .append(":\n\t")
                    .append(artist.getEvents().getFirst().getDescription());
        }
        for (int eventIndex = 1; eventIndex < artist.getEvents().size(); eventIndex++) {
            builder.append(", ")
                    .append(artist.getEvents().get(eventIndex).getName())
                    .append(" - ")
                    .append(artist.getEvents().get(eventIndex).getDate())
                    .append(":\n\t")
                    .append(artist.getEvents().get(eventIndex).getDescription());
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public boolean isCreatedByUser(final String username) {
        return artist.getName().equals(username);
    }

    @Override
    public AddMerchResult acceptBuyMerch(final String name) {
        return artist.buyMerch(name) ? AddMerchResult.SUCCESSFUL_PURCHASE
                                        : AddMerchResult.NONEXISTENT_MERCH;
    }

    @Override
    public String acceptSubscribe(final NormalUser normalUser) {
        if (artist.getSubject().subscribe(normalUser)) {
            return normalUser.getName() + " subscribed to " + artist.getName() + " successfully.";
        }

        return normalUser.getName() + " unsubscribed from " + artist.getName() + " successfully.";
    }
}
