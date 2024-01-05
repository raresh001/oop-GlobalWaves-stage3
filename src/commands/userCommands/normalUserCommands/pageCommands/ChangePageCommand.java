package commands.userCommands.normalUserCommands.pageCommands;

import admin.Admin;
import audio.audioCollections.Album;
import audio.audioCollections.Podcast;
import audio.audioFiles.Song;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import commands.userCommands.normalUserCommands.NormalUserCommand;
import fileio.input.CommandInput;
import user.artist.Artist;
import user.host.Host;
import user.normalUser.page.ArtistPage;
import user.normalUser.page.HomePage;
import user.normalUser.page.HostPage;
import user.normalUser.page.LikedContentPage;
import user.normalUser.player.PlayableEntity;

public final class ChangePageCommand extends NormalUserCommand {
    private final String nextPage;
    public ChangePageCommand(final CommandInput commandInput) {
        super(commandInput);
        nextPage = commandInput.getNextPage();
    }

    private HostPage getHostPage() {
        PlayableEntity playableEntity = normalUser.getPlayer().getPlayingAudioFile();
        if (playableEntity == null
                || playableEntity.getType() != PlayableEntity.AudioType.PODCAST) {
            return null;
        }

        Host host = ((Podcast) playableEntity).getHost();
        if (host == null) {
            return null;
        }

        return new HostPage(host);
    }

    private ArtistPage getArtistPage() {
        PlayableEntity playableEntity = normalUser.getPlayer().getPlayingAudioFile();
        if (playableEntity == null
                || playableEntity.getType() == PlayableEntity.AudioType.PLAYLIST
                || playableEntity.getType() == PlayableEntity.AudioType.PODCAST) {
            return null;
        }

        if (playableEntity.getType() == PlayableEntity.AudioType.SONG) {
            return new ArtistPage(Admin.getArtistByName(((Song) playableEntity).getArtist()));
        } else {
            return new ArtistPage(Admin.getArtistByName(((Album) playableEntity).getOwner()));
        }
    }

    /**
     * Executes change page command
     * @param output - the ArrayNode containing command's result
     */
    @Override
    public void executeCommand(final ArrayNode output) {
        ObjectNode objectNode = createTemplateResultRequireOnline("changePage", output);
        if (objectNode == null) {
            return;
        }

        normalUser.getPlayer().updatePosition(timestamp);
        switch (nextPage) {
            case "Home":
                normalUser.changePage(new HomePage(normalUser));
                break;
            case "LikedContent":
                normalUser.changePage(new LikedContentPage(normalUser));
                break;
            case "Artist":
                ArtistPage artistPage = getArtistPage();
                if (artistPage == null) {
                    objectNode.put("message", username
                            + " is trying to access a non-existent page.");
                    output.add(objectNode);
                    return;
                }
                normalUser.changePage(artistPage);
                break;
            case "Host":
                HostPage hostPage = getHostPage();
                if (hostPage == null) {
                    objectNode.put("message", username
                            + " is trying to access a non-existent page.");
                    output.add(objectNode);
                    return;
                }
                normalUser.changePage(hostPage);
                break;
            default:
                objectNode.put("message", username
                                                    + " is trying to access a non-existent page.");
                output.add(objectNode);
                return;
        }

        objectNode.put("message", username + " accessed " + nextPage + " successfully.");
        output.add(objectNode);
    }
}
