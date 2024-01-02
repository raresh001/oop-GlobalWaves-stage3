package audio.audioCollections;

import audio.audioFiles.Song;
import lombok.Getter;
import user.normalUser.player.PlayableEntity;
import user.normalUser.player.Player;

import java.util.List;

@Getter
public abstract class SongsCollection extends PlayableEntity {
    protected List<Song> songs;
    private final String owner;

    public SongsCollection(final String name1, final String owner1) {
        super(name1);
        owner = owner1;
    }

    @Override
    public final String getTrackName(final Player player) {
        return songs.get(player.getPosition().getTrack()).getName();
    }

    @Override
    public final int getRemainingTime(final Player player) {
        return songs.get(player.getPosition().getTrack()).getDuration()
                - player.getPosition().getPositionInTrack();
    }

    @Override
    public final String repeatState(final int repeat) {
        if (repeat == 0) {
            return "No Repeat";
        }

        if (repeat == 1) {
            return "Repeat All";
        }

        return "Repeat Current Song";
    }

    @Override
    public final boolean canBeDeleted() {
        for (Song song : getSongs()) {
            if (!song.canBeDeleted()) {
                return false;
            }
        }

        return super.canBeDeleted();
    }

    @Override
    public final void watch(final Player player) {
        super.watch(player);
        for (Song song : songs) {
            song.watch(player);
        }
    }

    @Override
    public final void unwatch(final Player player) {
        super.unwatch(player);
        for (Song song : songs) {
            song.unwatch(player);
        }
    }
}
