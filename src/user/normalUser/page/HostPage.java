package user.normalUser.page;

import audio.audioCollections.Podcast;
import user.host.Host;
import user.normalUser.NormalUser;

public final class HostPage implements Page {
    private final Host host;

    public HostPage(final Host host1) {
        host = host1;
    }

    private static String podcastInfoToString(final Podcast podcast) {
        final StringBuilder builder = new StringBuilder(podcast.getName());

        builder.append(":\n\t[");
        if (!podcast.getEpisodes().isEmpty()) {
            builder.append(podcast.getEpisodes().get(0).getName())
                    .append(" - ")
                    .append(podcast.getEpisodes().get(0).getDescription());

            for (int episodeIndex = 1;
                 episodeIndex < podcast.getEpisodes().size();
                 episodeIndex++) {
                builder.append(", ")
                        .append(podcast.getEpisodes().get(episodeIndex).getName())
                        .append(" - ")
                        .append(podcast.getEpisodes().get(episodeIndex).getDescription());
            }
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public String printPage() {
        final StringBuilder builder = new StringBuilder("Podcasts:\n\t[");

        if (!host.getPodcasts().isEmpty()) {
            builder.append(podcastInfoToString(host.getPodcasts().get(0)));

            for (int podcastIndex = 1; podcastIndex < host.getPodcasts().size(); podcastIndex++) {
                builder.append("\n, ")
                        .append(podcastInfoToString(host.getPodcasts().get(podcastIndex)));
            }
        }

        builder.append("\n]\n\nAnnouncements:\n\t[");

        if (!host.getAnnouncements().isEmpty()) {
            builder.append(host.getAnnouncements().get(0).getName())
                    .append(":\n\t")
                    .append(host.getAnnouncements().get(0).getDescription())
                    .append("\n");

            for (int announcementIndex = 1;
                 announcementIndex < host.getAnnouncements().size();
                 announcementIndex++) {
                builder.append(", ")
                        .append(host.getAnnouncements().get(announcementIndex).getName())
                        .append(":\n\t")
                        .append(host.getAnnouncements().get(announcementIndex).getDescription())
                        .append("\n");
            }
        }

        builder.append("]");

        return builder.toString();
    }

    @Override
    public boolean isCreatedByUser(final String username) {
        return host.getName().equals(username);
    }

    @Override
    public String acceptSubscribe(final NormalUser normalUser) {
        if (host.getSubject().subscribe(normalUser)) {
            return normalUser.getName() + " subscribed to " + host.getName() + " successfully.";
        }

        return normalUser.getName() + " unsubscribed from " + host.getName() + " successfully.";
    }
}
