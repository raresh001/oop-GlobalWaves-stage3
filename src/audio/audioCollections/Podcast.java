package audio.audioCollections;

import admin.Admin;
import audio.audioFiles.Episode;
import fileio.input.EpisodeInput;
import fileio.input.PodcastInput;
import lombok.Getter;
import user.host.Host;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;

import java.util.ArrayList;

@Getter
public final class Podcast extends PlayableEntity {
    public static final int FORWARD_BACKWARD_TIME = 90;
    private final ArrayList<Episode> episodes;
    private Host host;
    private final String owner;

    public Podcast(final PodcastInput podcastInput) {
        super(podcastInput.getName());
        this.owner = podcastInput.getOwner();

        episodes = new ArrayList<>();
        for (EpisodeInput episodeInput : podcastInput.getEpisodes()) {
            episodes.add(new Episode(episodeInput));
        }
    }

    public Podcast(final String name, final ArrayList<Episode> episodes1, final String owner1) {
        super(name);
        episodes = episodes1;
        owner = owner1;
        host = Admin.getHostByName(owner1);
    }

    @Override
    public AudioType getType() {
        return AudioType.PODCAST;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getTrackName(final Player player) {
        return episodes.get(player.getPosition().getTrack()).getName();
    }

    @Override
    public int getRemainingTime(final Player player) {
        return episodes.get(player.getPosition().getTrack()).getDuration()
                            - player.getPosition().getPositionInTrack();
    }
}
