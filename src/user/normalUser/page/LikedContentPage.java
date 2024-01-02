package user.normalUser.page;

import user.normalUser.NormalUser;

public final class LikedContentPage implements Page {
    private final NormalUser normalUser;

    public LikedContentPage(final NormalUser normalUser1) {
        normalUser = normalUser1;
    }

    @Override
    public String printPage() {
        final StringBuilder builder = new StringBuilder("Liked songs:\n\t[");

        if (!normalUser.getLikedSongs().isEmpty()) {
            builder.append(normalUser.getLikedSongs().get(0).getName())
                    .append(" - ")
                    .append(normalUser.getLikedSongs().get(0).getArtist());
            for (int songIndex = 1; songIndex < normalUser.getLikedSongs().size(); songIndex++) {
                builder.append(", ")
                        .append(normalUser.getLikedSongs().get(songIndex).getName())
                        .append(" - ")
                        .append(normalUser.getLikedSongs().get(songIndex).getArtist());
            }
        }

        builder.append("]\n\nFollowed playlists:\n\t[");

        if (!normalUser.getFollowedPlaylists().isEmpty()) {
            builder.append(normalUser.getFollowedPlaylists().get(0).getName())
                    .append(" - ")
                    .append(normalUser.getFollowedPlaylists().get(0).getOwner());

            for (int playlistIndex = 1;
                 playlistIndex < normalUser.getFollowedPlaylists().size();
                 playlistIndex++) {
                builder.append(normalUser.getFollowedPlaylists().get(playlistIndex).getName())
                        .append(" - ")
                        .append(normalUser.getFollowedPlaylists().get(playlistIndex).getOwner());
            }
        }

        return builder.append("]").toString();
    }
}
