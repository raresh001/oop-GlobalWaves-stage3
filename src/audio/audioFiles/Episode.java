package audio.audioFiles;

import fileio.input.EpisodeInput;
import lombok.Getter;
import user.normalUser.player.Listenable;
import user.normalUser.player.Player;

import java.util.HashMap;

@Getter
public final class Episode implements Listenable {
    private final String name;
    private final int duration;
    private final String description;
    private final HashMap<String, Integer> listeners = new HashMap<>();

    public Episode(final EpisodeInput episodeInput) {
        name = episodeInput.getName();
        duration = episodeInput.getDuration();
        description = episodeInput.getDescription();
    }

    @Override
    public void acceptListen(int value, Player player) {
        String username = player.getUsername();
        listeners.put(username, listeners.getOrDefault(username, 0) + value);

        player.listen(this, value);
    }
}
