package audio.audioCollections;

import admin.Admin;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Getter;
import user.normalUser.NormalUser;
import user.normalUser.Notification;
import java.util.LinkedList;
import java.util.List;

@Getter
public final class Playlist extends SongsCollection {
    private boolean isPublic = true;
    private final List<NormalUser> followingUsers = new LinkedList<>();

    public Playlist(final String name1, final String owner1) {
        super(name1, owner1);
        songs = new LinkedList<>();
    }

    @Override
    public AudioType getType() {
        return AudioType.PLAYLIST;
    }

    /**
     * changes the public status of the playlist
     * @return - the new public status
     */
    public boolean resetIsPublic() {
        isPublic = !isPublic;
        return isPublic;
    }

    /**
     * follow/unfollow the current playlist
     * @param normalUser1 - the normal user that commanded follow
     * @param objectNode - the json containing the result of this command
     */
    public void followPlaylist(final NormalUser normalUser1, final ObjectNode objectNode) {
        if (getOwner().equals(normalUser1.getName())) {
            objectNode.put("message",
                        "You cannot follow or unfollow your own playlist.");
            return;
        }

        if (normalUser1.getFollowedPlaylists().contains(this)) {
            normalUser1.getFollowedPlaylists().remove(this);
            followingUsers.remove(normalUser1);
            objectNode.put("message", "Playlist unfollowed successfully.");
        } else {
            normalUser1.getFollowedPlaylists().add(this);
            followingUsers.add(normalUser1);
            objectNode.put("message", "Playlist followed successfully.");
            Admin.getNormalUserByName(getOwner())
                    .getNotifications()
                    .add(new Notification("Playlist followed",
                                                "Playlist "
                                                            + getName()
                                                            + " is now followed by "
                                                            + normalUser1.getName()
                                                            + "."));
        }
    }

    public int getNoFollowers() {
        return followingUsers.size();
    }
}
