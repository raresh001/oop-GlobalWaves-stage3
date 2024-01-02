package user.normalUser.player;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public final class Position {
    private int track;
    private int positionInTrack;

    public Position(final int track1, final int positionInTrack1) {
        track = track1;
        positionInTrack = positionInTrack1;
    }
}
