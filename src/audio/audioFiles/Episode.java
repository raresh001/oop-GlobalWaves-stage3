package audio.audioFiles;

import fileio.input.EpisodeInput;
import lombok.Getter;
import user.normalUser.player.Listenable;
import user.normalUser.player.Player;

import java.util.HashMap;

@Getter
public final class Episode {
    private final String name;
    private final int duration;
    private final String description;
    private final HashMap<String, Integer> listeners = new HashMap<>();

    public Episode(final EpisodeInput episodeInput) {
        name = episodeInput.getName();
        duration = episodeInput.getDuration();
        description = episodeInput.getDescription();
    }
}
